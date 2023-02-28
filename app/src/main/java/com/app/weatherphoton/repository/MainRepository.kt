package com.app.weatherphoton.repository

import com.app.weatherphoton.networkService.ApiServiceImpl
import com.app.weatherphoton.networkService.AppAPi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepository @Inject constructor(private var apiServiceImpl: ApiServiceImpl) {

    fun getInformation(lat: Double, lon: Double) = flow {
        val response = apiServiceImpl.getInformation(lat, lon, AppAPi.appKey)
        emit(response)
    }.flowOn(Dispatchers.IO)

    fun getCityNameDataInformation(cityName: String) = flow {
        emit(apiServiceImpl.getInformationCityName(cityName, AppAPi.appKey))
    }.flowOn(Dispatchers.IO)

}