package com.example.smartsalesvisit.common


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.provider.Settings
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false ||
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
}

fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@SuppressLint("MissingPermission")
fun getCurrentLocation(
    context: Context,
    onSuccess: (Double, Double) -> Unit,
    onError: (String) -> Unit
) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onSuccess(location.latitude, location.longitude)
        } else {
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { freshLocation ->
                if (freshLocation != null) {
                    onSuccess(freshLocation.latitude, freshLocation.longitude)
                } else {
                    onError("Unable to fetch location. Try again.")
                }
            }.addOnFailureListener {
                onError("Error getting location: ${it.message}")
            }
        }
    }.addOnFailureListener {
        onError("Error getting location: ${it.message}")
    }
}

suspend fun getAddressFromLatLng(context: Context, lat: Double, lon: Double): Address? {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val list = geocoder.getFromLocation(lat, lon, 1)
            list?.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
