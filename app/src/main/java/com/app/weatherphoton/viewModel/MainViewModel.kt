package com.app.weatherphoton.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.weatherphoton.networkService.ApiState
import com.app.weatherphoton.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private var mainRepository: MainRepository) : ViewModel() {

    private val myDataInformation: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val myData: StateFlow<ApiState> = myDataInformation

    init {
        getDataInformation(40.714, -74.006)
    }

    fun getDataInformation(lat : Double, lon: Double) = viewModelScope.launch {
        myDataInformation.value = ApiState.Loading
        mainRepository.getInformation(lat, lon)
            .catch { e -> myDataInformation.value = ApiState.Failure(e) }
            .collect { data -> myDataInformation.value = ApiState.Success(data) }
    }


    fun getCityNameDataInformation(cityName: String) = viewModelScope.launch {
        myDataInformation.value = ApiState.Loading
        mainRepository.getCityNameDataInformation(cityName)
            .catch { e -> myDataInformation.value = ApiState.Failure(e) }
            .collect { data -> myDataInformation.value = ApiState.Success(data) }
    }


}