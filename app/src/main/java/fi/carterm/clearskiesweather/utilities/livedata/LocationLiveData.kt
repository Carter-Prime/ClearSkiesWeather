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
import fi.carterm.clearskiesweather.models.misc.LocationDetails

class LocationLiveData(context: Context) : LiveData<LocationDetails>() {

    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    var appContext = context
    val address: String = ""

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
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location.also {
                    if (it != null) {
                        setLocationData(it, address)
                    }
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
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult!!)
            for (location in locationResult.locations) {
                    val address = getAddress(location.latitude, location.longitude, appContext)
                    setLocationData(location, address)
                }
        }
    }

    private fun setLocationData(location: Location, address: String) {
        value = LocationDetails(location.latitude.toString(), location.longitude.toString(), address)
    }

    private fun getAddress(lat: Double, lng: Double, context: Context): String {
        return if (Geocoder.isPresent()) {
            val geocoder = Geocoder(context)
            val list = geocoder.getFromLocation(
                lat,
                lng, 1)
            list[0].getAddressLine(0)
        } else {
            "Geocoder is not available"
        }

    }

    companion object {
        private const val ONE_MINUTE: Long = 60000
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = ONE_MINUTE
            fastestInterval = ONE_MINUTE / 4
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

    }


}

