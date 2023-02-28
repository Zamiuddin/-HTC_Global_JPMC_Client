package com.app.weatherphoton.networkService

import com.example.example.ExampleJson2KtKotlin
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("data/2.5/weather")
    suspend fun getInformation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): ExampleJson2KtKotlin

    @GET("data/2.5/weather")
    suspend fun getInformationCityName(
        @Query("q") cityName: String,
        @Query("appid") appid: String
    ): ExampleJson2KtKotlin


}