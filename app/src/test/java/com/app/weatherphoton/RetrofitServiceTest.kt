package com.app.weatherphoton

import com.app.weatherphoton.networkService.ApiService
import com.google.gson.Gson
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceTest {

    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiService
    lateinit var gson: Gson

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}