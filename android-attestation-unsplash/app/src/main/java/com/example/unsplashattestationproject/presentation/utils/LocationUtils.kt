package com.example.unsplashattestationproject.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.unsplashattestationproject.data.dto.photos.Location

object LocationUtils {

    internal fun getLocationRequest(location: Location): String {
        val position = location.position
        return if (position?.latitude != null && position.longitude != null) {
            "geo:${position.latitude},${position.longitude}"
        } else {
            val cityCountry =
                listOfNotNull(location.city, location.country).joinToString(separator = ",")
            getNoLatLongLocationRequest(cityCountry)
        }
    }

    internal fun getNoLatLongLocationRequest(location: String): String {
        return "geo:0,0?q=$location"
    }

    internal fun showLocationOnMap(context : Context, location: String) {
        val geoUri = Uri.parse(location)
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            Toast.makeText(context, "Map app is not found", Toast.LENGTH_LONG).show()
        }
    }
}