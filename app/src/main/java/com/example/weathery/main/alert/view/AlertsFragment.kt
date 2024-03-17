package com.example.weathery.main.alert.view

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.example.weathery.R
import com.example.weathery.data.database.AlertsLocalDataSourceImpl
import com.example.weathery.data.models.Alert
import com.example.weathery.data.repositories.AlertsRepositoryImpl
import com.example.weathery.data.utils.AlarmReceiver
import com.example.weathery.data.utils.CustomAlertDialog
import com.example.weathery.databinding.FragmentAlertsBinding
import com.example.weathery.main.alert.viewModel.AlertsViewModel
import com.example.weathery.main.alert.viewModel.AlertsViewModelFactory
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlertsFragment : Fragment() {

    private lateinit var binding: FragmentAlertsBinding
    private lateinit var alertsViewModel: AlertsViewModel
    private lateinit var alertDialog: CustomAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAlertsBinding.inflate(inflater, container, false)
        alertDialog = CustomAlertDialog(requireContext())
        setupViewModels()
        setupRecyclerView()
        setupFabClickListener()
        requirePermissions()
        alertsViewModel.getAlertList()
        return binding.root
    }

    private fun setupViewModels() {
        val alertsRepository = AlertsRepositoryImpl.getInstance(AlertsLocalDataSourceImpl.getInstance(requireContext()))
        val alertsViewModelFactory = AlertsViewModelFactory(alertsRepository)
        alertsViewModel = ViewModelProvider(this, alertsViewModelFactory).get(AlertsViewModel::class.java)
    }

    private fun setupRecyclerView() {
        val alertsAdapter = AlertsAdapter(requireContext()) { alert ->
            alertsViewModel.deleteAlert(alert)
            WorkManager.getInstance(requireContext()).cancelAllWorkByTag(alert.id.toString())
        }
        binding.recyclerViewAlarms.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = alertsAdapter
        }
        alertsViewModel.alertsList.observe(viewLifecycleOwner) { list ->
            alertsAdapter.submitList(list)
            binding.groupNoAlarm.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }
        val itemTouchHelper = ItemTouchHelper(getItemTouchHelperCallback(alertsAdapter))
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewAlarms)
    }

    private fun getItemTouchHelperCallback(adapter: AlertsAdapter): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val alert = adapter.currentList[position]
                alertsViewModel.deleteAlert(alert)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean,
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addBackgroundColor(requireContext().getColor(R.color.red))
                    .addActionIcon(R.drawable.ic_del)
                    .create()
                    .decorate()
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }

    private fun setupFabClickListener() {
        binding.fabAddAlarm.setOnClickListener {
            showAddAlarmDialog()
        }
    }

    private fun showAddAlarmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
        val saveButton = view.findViewById<Button>(R.id.btn_save)
        val endDateCardView = view.findViewById<CardView>(R.id.card_view_enddate)
        val startTimeCardView = view.findViewById<CardView>(R.id.card_view_starttime)
        val endTimeCardView = view.findViewById<CardView>(R.id.card_view_endtime)
        val txtEndDate = view.findViewById<TextView>(R.id.txt_enddate)
        val txtStartTime = view.findViewById<TextView>(R.id.txt_starttime)
        val txtEndTime = view.findViewById<TextView>(R.id.txt_endtime)

        val currentCalendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(currentCalendar.time)

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val currentTime = timeFormat.format(currentCalendar.time)

        builder.setView(view)
        builder.setCancelable(false)

        val alertDialog = builder.create()

        endDateCardView.setOnClickListener {
            openDatePicker(txtEndDate)
        }

        startTimeCardView.setOnClickListener {
            openTimePicker(txtStartTime)
        }

        endTimeCardView.setOnClickListener {
            openTimePicker(txtEndTime)
        }

        saveButton.setOnClickListener {
            val alertId = alertsViewModel.insertAlert(Alert(endDate = txtEndDate.text.toString(),
                startTime = txtStartTime.text.toString(),
                endTime = txtEndTime.text.toString(),
                alertType = getString(R.string.alarm)))
            scheduleAlarm(alertId, txtEndDate.text.toString(), txtEndTime.text.toString())
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun openDatePicker(dateView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, day)
                }
                updateDateView(selectedDate, dateView)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun openTimePicker(timeView: TextView) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val is24HourFormat = DateFormat.is24HourFormat(requireContext())
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                }
                updateTimeView(selectedTime, timeView)
            },
            hour,
            minute,
            is24HourFormat
        )
        timePickerDialog.show()
    }

    private fun updateDateView(selectedDate: Calendar, dateView: TextView) {
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)
        dateView.text = formattedDate
    }

    private fun updateTimeView(selectedTime: Calendar, timeView: TextView) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(selectedTime.time)
        timeView.text = formattedTime
    }


    private fun scheduleAlarm(alertId: Long, selectedDate: String, selectedTime: String) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar.time = dateFormat.parse(selectedDate)!!
        val selectedTimeCalendar = Calendar.getInstance()
        selectedTimeCalendar.time = timeFormat.parse(selectedTime)!!

        calendar.set(Calendar.YEAR, selectedDateCalendar.get(Calendar.YEAR))
        calendar.set(Calendar.MONTH, selectedDateCalendar.get(Calendar.MONTH))
        calendar.set(Calendar.DAY_OF_MONTH, selectedDateCalendar.get(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, selectedTimeCalendar.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, selectedTimeCalendar.get(Calendar.MINUTE))

        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("id", alertId)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun requirePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            requireActivity().setShowWhenLocked(true)
            requireActivity().setTurnScreenOn(true)
        }
        if (!Settings.canDrawOverlays(requireActivity())) {
            checkPermissionsDialog()
        }
    }

    private fun checkPermissionsDialog() {
        alertDialog.showCustomDialog(
            getString(R.string.permission_request),
            getString(R.string.alarm_permission),
            getString(R.string.allow),
            getString(R.string.dismiss),
            { _, _ -> checkAppPermission() },
            { _, _ -> showPermissionsWarning() }
        ){}
    }

    private fun checkAppPermission() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + requireContext().packageName)
            )
            someActivityResultLauncher.launch(intent)
        }
    }

    private fun showPermissionsWarning() {
        alertDialog.showSimpleAlert(
            getString(R.string.warning),
            getString(R.string.permission_not_granted)
        )
    }

    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (!Settings.canDrawOverlays(requireContext())) {
            Snackbar.make(
                binding.root,
                getString(R.string.permission_not_granted),
                Toast.LENGTH_LONG
            ).setAction("Enable") {
                sendToEnableIt()
            }.show()
        }
    }
    private fun sendToEnableIt() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + requireContext().packageName)
        )
        someActivityResultLauncher.launch(intent)
    }


}
