package com.app.weatherphoton.networkService

import com.example.example.ExampleJson2KtKotlin
import javax.inject.Inject

class ApiServiceImpl @Inject constructor(private var apiService: ApiService) {

    suspend fun getInformation(
        lat: Double,
        lon: Double,
        appid: String
    ): ExampleJson2KtKotlin = apiService.getInformation(lat, lon, appid)


    suspend fun getInformationCityName(
        cityName: String,
        appid: String
    ): ExampleJson2KtKotlin = apiService.getInformationCityName(cityName, appid)
}