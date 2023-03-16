package com.app.weatherphoton

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.app.weatherphoton.networkService.ApiService
import com.app.weatherphoton.networkService.ApiServiceImpl
import com.app.weatherphoton.networkService.ApiState
import com.app.weatherphoton.networkService.AppAPi
import com.app.weatherphoton.repository.MainRepository
import com.app.weatherphoton.viewModel.MainViewModel
import com.example.example.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @Mock
    lateinit var mainViewModel: MainViewModel

    @Mock
    lateinit var mainRepository: MainRepository

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var apiServiceImpl: ApiServiceImpl

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = mock(MainRepository::class.java)
        mainViewModel = MainViewModel(mainRepository)
    }

    @Test
    fun getInformation() {

        runTest {
            doReturn(
                ApiState.Success(
                    flowOf(
                        ExampleJson2KtKotlin()
                    )
                )
            ).`when`(apiService).getInformation(40.714, -74.006, AppAPi.appKey)
            mainViewModel.myData.test {
                assertEquals(
                    ApiState.Success(
                        (ExampleJson2KtKotlin())
                    ), awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(apiService).getInformation(40.714, -74.006, AppAPi.appKey)
        }
    }

    @Test
    fun getInformationError() {
        runTest {
            val errorMessage = "Error Message For You"
            doReturn(flowOf {
                throw java.lang.IllegalStateException(errorMessage)
            }).`when`(apiService).getInformation(40.714, -74.006, AppAPi.appKey)
            mainViewModel.myData.test {
                assertEquals(ApiState.Failure(IllegalStateException(errorMessage)), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            verify(apiService).getInformation(40.714, -74.006, AppAPi.appKey)
        }
    }

    @Test
    fun getCityNameDataInformationTest() {

        runTest {
            doReturn(
                ApiState.Success(
                    flowOf(
                        ExampleJson2KtKotlin()
                    )
                )
            ).`when`(apiService).getInformationCityName("London", AppAPi.appKey)
            mainViewModel.myData.test {
                assertEquals(
                    ApiState.Success(
                        (ExampleJson2KtKotlin())
                    ), awaitItem()
                )
                cancelAndIgnoreRemainingEvents()
            }
            verify(apiService).getInformationCityName("London", AppAPi.appKey)
        }
    }


}