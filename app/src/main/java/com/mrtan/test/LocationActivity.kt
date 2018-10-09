package com.mrtan.test

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import kotlinx.android.synthetic.main.activity_main.*

class LocationActivity : AppCompatActivity() {
  private lateinit var lm: LocationManager

  @SuppressLint("MissingPermission")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    AndPermission
      .with(this)
      .runtime()
      .permission(Permission.Group.LOCATION)
      .onGranted {
        val location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
          lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1f, object : LocationListener {
            override fun onLocationChanged(location: Location) {
              text.text = location.toString()
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onProviderDisabled(provider: String?) {
            }
          })
        } else {
          text.text = location.toString()
        }
      }.start()
  }
}