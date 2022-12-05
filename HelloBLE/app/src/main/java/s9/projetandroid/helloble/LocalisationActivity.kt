package s9.projetandroid.helloble

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.snackbar.Snackbar
import s9.projetandroid.helloble.databinding.ActivityLocalisationBinding
import java.util.*

class LocalisationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocalisationBinding

    private val PERMISSION_REQUEST_LOCATION = 1

    private val targetIntent = Intent().apply {
        action = android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalisationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LocalisationButton.setOnClickListener {
            requestPermission()
        }

        supportActionBar?.apply {
            title = binding.LocalisationTextView.text
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (hasPermission()) {
            var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.getCurrentLocation(PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
                .addOnSuccessListener { geoCode(it) }
                .addOnFailureListener {
                    Snackbar.make(binding.root, "Erreur de localisation", Snackbar.LENGTH_LONG).show()
                }
        }
    }

    private fun geoCode(location: Location){
        val geocoder = Geocoder(this, Locale.getDefault())
        val results = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        if (results.isNotEmpty()) {
            binding.LocalisationTextView.text = results[0].getAddressLine(0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission obtenue, Nous continuons la suite de la logique.
                    getLocation()
                } else {
                    // TODO
                    // Permission non accepté, expliqué ici via une activité ou une dialog pourquoi nous avons besoin de la permission

                    startActivity(targetIntent)

                }
                return
            }
        }
    }

    private fun requestPermission() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_LOCATION)
        } else {
            getLocation()
        }
    }

    private fun hasPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}