package com.ocsen.onestep

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ocsen.onestep.databinding.ActivityMainBinding
import com.ocsen.onestep.services.ForegroundLocationService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val REQUEST_LOCATION = 1011
    val REQUEST_CAMERA = 1012

    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()

        if (checkPermission(Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA
            )
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                AlertDialog.Builder(this)
                    .setMessage("Need camera permission to capture image. Please provide permission to access your camera.")
                    .setPositiveButton("OK") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            REQUEST_CAMERA
                        )
                    }
                    .setNegativeButton("Cancel") { dialogInterface, i ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show();
            }
        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION
        )
        viewModel.toggleLocationUpdates()
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA) {

            Log.d("REQUEST_CAMERA", "")
        } else if (requestCode == REQUEST_LOCATION) {
            Log.d("REQUEST_LOCATION", "")
        }
    }


    override fun onStart() {
        super.onStart()
        val serviceIntent = Intent(this, ForegroundLocationService::class.java)
        bindService(serviceIntent, viewModel, BIND_AUTO_CREATE)
    }
    override fun onStop() {
        super.onStop()
        unbindService(viewModel)
    }
}