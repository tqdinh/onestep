package com.ocsen.onestep

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.data.local.entities.LocalImage
import com.example.firstosproject.reviewImage.ReviewFragment
import com.ocsen.onestep.Utils.Util
import com.ocsen.onestep.databinding.ActivityCameraPreviewBinding
import com.ocsen.onestep.view.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraPreview : AppCompatActivity(), LifecycleOwner {
    val viewModel: PreviewViewModel by viewModels()
    private var progressDialog: ProgressDialog? = null


    val PHOTO_EXTENSION = ".jpg"
    private val imageCapture: ImageCapture by lazy {
        ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY).build()
    }

    private lateinit var rawPath: String
    private lateinit var cameraExecutor: ExecutorService
    val REQUEST_LOCATION = 1011
    val REQUEST_CAMERA = 1012
    var currentlat = 0.0
    var currentLon = 0.0
    var placeId = ""


    private lateinit var binding: ActivityCameraPreviewBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra("PLACEID"))
            placeId = intent.getStringExtra("PLACEID")!!
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding = ActivityCameraPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startCamera()

        progressDialog = ProgressDialog(this)

//        binding.tvLat.text = previewViewModel.latCoordinate.toString()
//        binding.tvLong.text = previewViewModel.longCoordinate.toString()
        if (checkPermission(Manifest.permission.CAMERA)) {

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
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_LOCATION
        )
        binding.btnTakePic.setOnClickListener({
            takePhoto(placeId)
        })

        binding.ivSwitchCamera.setOnClickListener({
            switchCamera()
        })

        binding.ivBack.setOnClickListener({
            onBackPressed()
        })

        lifecycleScope.launch {
            viewModel.lastLocation.collect {
                it?.let {
                    Log.d("--LOCATION--", "${it.latitude},--,${it.longitude}")
                    currentlat = it.latitude
                    currentLon = it.longitude
                    binding.tvLat.text = it.latitude.toString()
                    binding.tvLong.text = it.longitude.toString()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect {
                if (it) {
                    progressDialog?.showDialog()
                } else {
                    progressDialog?.dismiss()
                }

            }
        }
    }

    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        // If the app name contains (e.g. dev app) a space, it is a not converted to %20 automatically.
        val appNameLowerUnderscore =
            "dinh_app".lowercase().replace(" ", "_")
        val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
            File(it, appNameLowerUnderscore).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    private fun onImageSaved(savedUri: Uri) {
        rawPath = savedUri.path.toString()
        Log.d("PATH_IMGE", rawPath)
    }

    private fun takePhoto(placeId: String) {
        viewModel.showLoading()
        val photoFile =
            File(getOutputDirectory(this), System.currentTimeMillis().toString() + PHOTO_EXTENSION)

        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputFileOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    Log.d("PATH_IMGE", "error+${error.toString()}")
                    // progressDialog?.dismiss()
                }

                @RequiresApi(Build.VERSION_CODES.N)
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)

                    savedUri?.let {
                        val jsonInfo = Util.getJsonString(
                            viewModel.lastLocation.value?.latitude.toString(),
                            viewModel.lastLocation.value?.longitude.toString()
                        )
                        ReviewFragment(savedUri.path.toString(),
                            jsonInfo, { fullPath ->
                                val fileToDelete = File(fullPath)
                                fileToDelete.delete()
                            }).show(supportFragmentManager, "review")

                        Util.mergeQRToImageWithPath(
                            savedUri.path.toString(),
                            jsonInfo
                        )?.let {
                            Util.saveFileBitmapWithPath(savedUri.path.toString(), it)
                        }
                        viewModel.addImageInfo(
                            LocalImage(
                                place_id = placeId, path = it.path.toString(), title = "title",
                                desc = "desc", lat = currentlat,
                                lon = currentLon
                            )
                        )
                        viewModel.hideLoading()

////                                previewViewModel.getPlaceListPlace().observe(this@CameraPreview, {
////                                    when (it.status) {
////                                        ResourceStatus.SUCCESS -> {
////                                            var places = it.data as ArrayList<PlaceInfo>
////                                            Log.d("_TABLE_", places.toString())
////                                        }
////                                        ResourceStatus.FAIL -> {
////                                            Log.d("FAILLL", it.message)
////                                        }
////                                    }
////                                })
//
//
//                        }
                        //onImageSaved(savedUri!!)
                    }


                }
            })
    }

    @SuppressLint("MissingPermission")
    fun getLatAndLong(context: Context): Pair<Double, Double> {
        var lat = currentlat
        var long = currentLon
        return lat to long
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(viewModel.lensFacing).build()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

                preview.setSurfaceProvider(binding.previewCam.surfaceProvider)

            } catch (exc: Exception) {
                Log.d("PATH_IMGE", "Failed to bind preview")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun switchCamera() {
        val lensFacing = if (viewModel.lensFacing == CameraSelector.LENS_FACING_BACK)
            CameraSelector.LENS_FACING_FRONT
        else
            CameraSelector.LENS_FACING_BACK

        viewModel.updateCameraLensFacing(lensFacing)

        startCamera()
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
}