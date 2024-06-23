package lnbti.gtp01.droidai.utils

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import lnbti.gtp01.droidai.constants.LocationConstants
import android.content.Context

/**
 * Helper class to manage location updates using the FusedLocationProviderClient.
 *
 * This class provides functionality to start and stop location updates and handle
 * location permission results. It uses a callback to deliver location updates to
 * the caller.
 *
 * @property context The context used to access system services.
 * @property onLocationReceived Callback invoked with the updated location.
 */
class LocationHelper(
    private val context: FragmentActivity,
    private val onLocationReceived: (Location) -> Unit
) {

    private var locationPermissionGranted: (() -> Unit)? = null
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    /**
     * Starts location updates if location permissions are granted.
     *
     * Requests location updates from the FusedLocationProviderClient and
     * delivers the results through the onLocationReceived callback. If
     * permissions are not granted, no action is taken.
     */
    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("LocationHelper", "+++ Location permission not granted")
            return
        }

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(3000)
            .setMaxUpdateDelayMillis(100)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                for (location in locationResult.locations) {
                    Log.d(
                        "LocationHelper",
                        "+++ Location received: Lat=${location.latitude}, Long=${location.longitude}"
                    )
                    onLocationReceived(location)
                    stopLocationUpdates()
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
        Log.d("LocationHelper", "+++ Location updates requested")
    }

    /**
     * Stops receiving location updates.
     *
     * Removes the location update callback from the FusedLocationProviderClient,
     * effectively stopping location updates.
     */
    fun stopLocationUpdates() {
        locationCallback?.let {
            Log.d("LocationHelper", "+++ Stopping location updates")
            fusedLocationClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }

    /**
     * Sets a callback to be invoked when location permissions are granted.
     *
     * @param callback The callback to be invoked.
     */
    fun setLocationPermissionGrantedCallback(callback: () -> Unit) {
        locationPermissionGranted = callback
    }

    /**
     * Handles the result of a location permission request.
     *
     * If the permissions are granted, the locationPermissionGranted callback is invoked.
     *
     * @param requestCode The request code associated with the permission request.
     * @param grantResults The results of the permission request.
     */
    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == LocationConstants.LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted?.invoke()
        }
    }

    /**
     * Checks whether the location services are enabled on the device.
     *
     * This function checks if either GPS or network-based location providers are enabled.
     *
     * @return `true` if at least one location provider is enabled, `false` otherwise.
     */
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}