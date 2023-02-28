package com.app.weatherphoton

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.weatherphoton.databinding.ActivityMainBinding
import com.app.weatherphoton.networkService.ApiState
import com.app.weatherphoton.viewModel.MainViewModel
import com.bumptech.glide.Glide
import com.example.example.ExampleJson2KtKotlin
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), LocationListener {

    private val TAG: String = "MainActivity"
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLocation: Location? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        activityMainBinding.searchInfo.setOnClickListener {
            if (activityMainBinding.searchInfoEdit.text.isNotEmpty()) {
                mainViewModel.getCityNameDataInformation(activityMainBinding.searchInfoEdit.text.toString())
            } else {
                showMessage("Please enter City/Zipcode/CityId")
            }
        }

        activityMainBinding.searchLocation.setOnClickListener {
            if (!checkPermissions()) {
                requestPermissions()
            } else {
                getLastLocation()
            }
        }

        lifecycleScope.launchWhenCreated {
            mainViewModel.myData.collect {
                when (it) {
                    is ApiState.Success -> {
                        loadDetails(it.data)
                    }
                    is ApiState.Loading -> {
                        Log.d(TAG, it.toString())
                    }
                    is ApiState.Failure -> {
                        showMessage(it.msg.toString())
                    }
                    else -> {}
                }
            }
        }


        Glide.with(this).load("https://openweathermap.org/img/wn/10d.png") // image url
            .placeholder(R.drawable.ic_launcher_background) // any placeholder to load at start
            .error(R.drawable.ic_launcher_background)  // any image in case of error
            .override(200, 200) // resizing
            .centerCrop().into(activityMainBinding.imageView)  // imageview object


    }

    private fun loadDetails(data: Any) {
        val jsonDataInformation: ExampleJson2KtKotlin = data as ExampleJson2KtKotlin
        activityMainBinding.tc1.text = jsonDataInformation.coord?.lat.toString()
        activityMainBinding.tc2.text = jsonDataInformation.coord?.lon.toString()
        activityMainBinding.tc3.text = jsonDataInformation.weather[0].main
        activityMainBinding.tc4.text = jsonDataInformation.weather[0].description
        activityMainBinding.tc5.text = jsonDataInformation.weather[0].icon
        activityMainBinding.tc6.text = jsonDataInformation.base
        activityMainBinding.tc7.text = jsonDataInformation.main?.temp.toString()
        activityMainBinding.tc8.text = jsonDataInformation.main?.feelsLike.toString()
        activityMainBinding.tc9.text = jsonDataInformation.main?.tempMin.toString()
        activityMainBinding.tc10.text = jsonDataInformation.main?.tempMax.toString()
        activityMainBinding.tc11.text = jsonDataInformation.main?.pressure.toString()
        activityMainBinding.tc12.text = jsonDataInformation.main?.humidity.toString()
        activityMainBinding.tc13.text = jsonDataInformation.visibility.toString()
        activityMainBinding.tc14.text = jsonDataInformation.wind?.speed.toString()
        activityMainBinding.tc15.text = jsonDataInformation.wind?.deg.toString()
        activityMainBinding.tc16.text = jsonDataInformation.rain?.h.toString()
        activityMainBinding.tc17.text = jsonDataInformation.clouds?.all.toString()
        activityMainBinding.tc18.text = jsonDataInformation.dt.toString()
        activityMainBinding.tc19.text = jsonDataInformation.sys?.country.toString()
        activityMainBinding.tc20.text =
            milliSecondsToDateTime(jsonDataInformation.sys?.sunrise!!.toLong())
        activityMainBinding.tc21.text =
            milliSecondsToDateTime(jsonDataInformation.sys?.sunset!!.toLong())
        activityMainBinding.tc22.text = jsonDataInformation.timezone.toString()
        activityMainBinding.tc23.text = jsonDataInformation.name.toString()
        activityMainBinding.tc24.text = jsonDataInformation.cod.toString()

    }

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation!!.addOnCompleteListener(this) { task ->
            if (task.isSuccessful && task.result != null) {
                lastLocation = task.result
                //latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude
                //longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude
                mainViewModel.getDataInformation(
                    (lastLocation)!!.latitude,
                    (lastLocation)!!.longitude
                )
            } else {
                Log.w(TAG, "getLastLocation:exception", task.exception)
                showMessage("No location detected. Make sure location is enabled on the device.")
            }
        }
    }

    private fun showMessage(string: String) {
        Toast.makeText(this@MainActivity, string, Toast.LENGTH_LONG).show()

    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this@MainActivity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        } else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(this@MainActivity, mainTextStringId, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "com.app.weatherphoton",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onLocationChanged(p0: Location) {
        Toast.makeText(
            this,
            "Latitude: " + p0.latitude + " , Longitude: " + p0.longitude,
            Toast.LENGTH_SHORT
        ).show()
    }


    fun milliSecondsToDateTime(long: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val dateString = simpleDateFormat.format(long)
        return dateString
    }
}