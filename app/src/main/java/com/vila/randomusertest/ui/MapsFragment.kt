package com.vila.randomusertest.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.vila.randomusertest.R
import com.vila.randomusertest.databinding.FragmentMapsBinding

class MapsFragment : Fragment(R.layout.fragment_maps), OnMapReadyCallback{


    private val PERMISSION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mFusedLocationClient :FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private var _viewBinding : FragmentMapsBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewBinding = FragmentMapsBinding.bind(view)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        init()
    }

    override fun onMapReady(map: GoogleMap) {
        mGoogleMap = map
    }

    private fun init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        if (checkForLocationPermissions()){
               getMyCurrentLocation()
        }else{
            askLocationPermissions()
        }
    }


    private fun checkForLocationPermissions() = PERMISSION.all {
            ContextCompat.checkSelfPermission(
                activity!!.applicationContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }

    private fun askLocationPermissions() {
        val requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission->
                if (permission.all { it.value }){
                    getMyCurrentLocation()
                    Log.d("mwebservice"," ++++++ All Permisses are accepted")
                }else {
                    Log.d("mwebservice", "++++++ Permisses not granted")
                }
            }
        requestPermission.launch(PERMISSION)
    }

    private fun getMyCurrentLocation() {
        try
        {
            val locationResult = mFusedLocationClient.lastLocation
            locationResult.addOnCompleteListener{
                    task -> if (task.isSuccessful)
            {
                if (task.getResult()!= null)
                {
                    lastLocation = task.result!!
                    with(mGoogleMap) {
                        moveCamera(
                            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastLocation.latitude,
                                    lastLocation.longitude
                                ), 15f
                            )
                        )
                    }
                    mGoogleMap.addMarker(MarkerOptions().position(LatLng(lastLocation.latitude,
                        lastLocation.longitude)).title(getString(R.string.Map_message)))
                }

            }
            }
        }catch (e :SecurityException)
        {
            Toast.makeText(activity,"Something wrong has happend .." + e, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}