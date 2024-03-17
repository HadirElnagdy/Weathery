package com.example.weathery.data.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CustomAlertDialog(private val context: Context) {


    private val builder = AlertDialog.Builder(context)
    private var alertDialog: AlertDialog? = null

    fun showCustomDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        positiveClickListener: (Any?, Any?) -> Unit,
        negativeClickListener: DialogInterface.OnClickListener,
        dismissListener: DialogInterface.OnDismissListener
    ) {
        alertDialog = MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, positiveClickListener)
            .setNegativeButton(negativeButtonText, negativeClickListener)
            .setOnDismissListener(dismissListener)
            .setCancelable(false)
            .show()
    }


    fun showSimpleAlert(title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null) // No action
            .show()
    }
}
