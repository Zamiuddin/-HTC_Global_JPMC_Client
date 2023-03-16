package com.app.weatherphoton

import com.app.weatherphoton.networkService.ApiServiceImpl
import com.app.weatherphoton.networkService.ApiState
import com.app.weatherphoton.networkService.AppAPi
import com.app.weatherphoton.repository.MainRepository
import com.example.example.ExampleJson2KtKotlin
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MainRepositoryTest {

    lateinit var mainRepository: MainRepository

    @Mock
    lateinit var apiServiceImpl: ApiServiceImpl

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        mainRepository = MainRepository(apiServiceImpl)
    }

    @Test
    fun getInformationtest() {
        runBlocking {
            Mockito.`when`(apiServiceImpl.getInformation(40.714, -74.006, AppAPi.appKey))
                .thenReturn(ExampleJson2KtKotlin())
            val response = mainRepository.getInformation(40.714, -74.006)
            assertEquals(ExampleJson2KtKotlin(), response.toString())
        }
    }

    @Test
    fun getCityNameDataInformationTest() {
        runBlocking {
            Mockito.`when`(apiServiceImpl.getInformationCityName("London", AppAPi.appKey))
                .thenReturn(ExampleJson2KtKotlin())
            val response = mainRepository.getCityNameDataInformation("London")
            assertEquals(ExampleJson2KtKotlin(), ApiState.Success(response).data)
        }
    }
}