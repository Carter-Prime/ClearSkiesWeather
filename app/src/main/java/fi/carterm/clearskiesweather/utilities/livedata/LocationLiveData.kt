package fi.carterm.clearskiesweather.utilities.livedata

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import fi.carterm.clearskiesweather.R
import fi.carterm.clearskiesweather.models.misc.LocationDetails
import kotlinx.coroutines.*


class LocationLiveData(context: Context) : LiveData<LocationDetails>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var appContext = context
    var address: String = ""

    override fun onInactive() {
        super.onInactive()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    override fun onActive() {
        super.onActive()
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
                location.also {
                    setLocationData(it.latitude, it.longitude, address)
                }
            }
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }

    }

    private val locationCallback = object : LocationCallback() {
        private val scope by lazy { CoroutineScope(SupervisorJob()) }
        override fun onLocationResult(locationResult: LocationResult) {

            super.onLocationResult(locationResult)
            for (location in locationResult.locations) {
               scope.launch {
                   val resp = getAddress(location.latitude, location.longitude, appContext)
                   withContext(Dispatchers.Main){
                       address = resp
                   }
               }
                setLocationData(location.latitude, location.longitude, address)
            }
        }
    }

    private fun setLocationData(lat: Double, long: Double, address: String?) {
        value = LocationDetails(lat.toString(), long.toString(), address)
    }

    private fun getAddress(lat: Double, lng: Double, context: Context): String {
        return if (Geocoder.isPresent()) {
            val geocoder = Geocoder(context)
            val list = geocoder.getFromLocation(
                lat,
                lng, 1)
            if(list.isNotEmpty()){
                list[0].getAddressLine(0)
            }else{
                appContext.getString(R.string.error_no_address_found)
            }

        } else {
            appContext.getString(R.string.error_geocoder_not_available)
        }

    }

    companion object {
        private const val ONE_MINUTE: Long = 60000
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE /6
            fastestInterval = ONE_MINUTE / 60
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    }


}

