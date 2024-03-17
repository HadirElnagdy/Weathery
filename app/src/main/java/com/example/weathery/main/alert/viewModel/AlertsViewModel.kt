package com.example.weathery.main.alert.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weathery.data.models.Alert
import com.example.weathery.data.repositories.AlertsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.lang.IllegalArgumentException

class AlertsViewModel(private val repo: AlertsRepository) : ViewModel(){

    private var _mutableAlertsList = MutableLiveData<List<Alert>>()
    val alertsList: LiveData<List<Alert>> = _mutableAlertsList


    fun insertAlert(alert: Alert) : Long{
        return runBlocking {
            repo.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: Alert){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAlert(alert)
        }
    }

    fun getAlertList() = viewModelScope.launch(Dispatchers.IO) {
        repo.getListOfAlerts().collectLatest {
            _mutableAlertsList.postValue(it)
        }
    }

    fun getAlertById(id: Long) = repo.getAlertWithId(id)

}

class AlertsViewModelFactory(private val repo: AlertsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlertsViewModel::class.java)){
            AlertsViewModel(repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}