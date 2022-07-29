package com.ocsen.onestep.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.CameraPreview
import com.ocsen.onestep.R
import com.ocsen.onestep.databinding.FragmentDashboardBinding
import com.ocsen.onestep.ui.adapter.LocalImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val viewModel: DashboardViewModel by viewModels()
    var localPlace: LocalPlace? = null
    var localImageAdapter: LocalImageAdapter? = null

    val line = Polyline().apply {
        this.getPaint().setPathEffect(
            android.graphics.DashPathEffect(
                kotlin.floatArrayOf(10f, 20f),
                0f
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        Configuration.getInstance()
            .load(requireContext(), PreferenceManager.getDefaultSharedPreferences(requireContext()))
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        binding.map.setBuiltInZoomControls(true)
        binding.map.setMultiTouchControls(true)
        val mapController = binding.map.controller
        mapController.setZoom(13)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
        viewModel.getPlaces()

        localPlace = arguments?.getParcelable<LocalPlace>("EVENT_DETAIL")

    }

    override fun onResume() {
        super.onResume()
        localPlace?.let {
            viewModel.getListOfImage(placeId = it.id)
        }
    }


    fun setupObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.lastLocation.collect {
                        it?.let {
                            Toast.makeText(
                                requireContext(),
                                "${it.latitude} ${it.longitude}",
                                Toast.LENGTH_SHORT
                            ).show()
                            // moveToGeo(it.latitude, it.longitude)
                        }
                    }
                }
                launch {
                    viewModel.listPlaces.collect {
                        it.size
                    }
                }

                launch {
                    viewModel.listImage.collect {
                        localImageAdapter?.updateListWithAllImage(arrayListOf())
                        localImageAdapter?.let { adapter ->
                            adapter.updateListWithAllImage(ArrayList(it))
                        }
                        val list_point = ArrayList<GeoPoint>()

                        it.forEach { localImage ->
                            list_point.add(GeoPoint(localImage.lat, localImage.lon))
                            addGeo(localImage.lat, localImage.lon, localImage.id)
                        }
                        drawPoliline(list_point)
                    }
                }

            }

        }

    }

    fun drawPoliline(geoList: ArrayList<GeoPoint>) {
        binding.map.overlayManager.remove(line)
        val geoPoints: ArrayList<GeoPoint> = arrayListOf()
        geoPoints.addAll(geoList)
        line.setPoints(geoPoints);
        binding.map.overlayManager.add(line)
    }

    fun openGeo(lat: Double, lng: Double) {
        val gmmIntentUri = Uri.parse("geo:${lat},${lng}?z=17&q=${lat},${lng}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    fun setupView() {
        localImageAdapter = LocalImageAdapter(object : LocalImageAdapter.OnItemClickListener {
            override fun onItemClick(item: LocalImage) {
                item.let {
                    openGeo(it.lat, it.lon)
                }

            }
        })
        val linearlayout =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvImages.apply {
            adapter = localImageAdapter
            itemAnimator = null
            layoutManager = linearlayout
            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this);

        }
        binding.rvImages.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            //
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (newState === RecyclerView.SCROLL_STATE_IDLE) {
                    val position = getCurrentItem()
                    val localimage = localImageAdapter?.getItem(position)
                    localimage?.let {
                        moveToGeo(it.lat, it.lon)
                    }

                }
            }

        })

        binding.fabCreate.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                createPlanName()
                return true
            }
        })

        binding.fabCreate.setOnClickListener {
            takePicture()
        }

        viewModel.lastLocation.value?.let {
            moveToGeo(it.latitude, it.longitude)
        }
    }

    private fun getCurrentItem(): Int {
        return (binding.rvImages.getLayoutManager() as LinearLayoutManager)
            .findFirstVisibleItemPosition()
    }

    fun takePicture() {
        viewModel.getLatitude()?.let { lat ->
            viewModel.getLongitude()?.let { lng ->
                localPlace?.let {
                    val intent = Intent(requireContext(), CameraPreview::class.java)
                    intent.putExtra("PLACEID", it.id)
                    intent.putExtra("LATCOOR", lat)
                    intent.putExtra("LONGCOOR", lng)
                    startActivity(intent)
                }

            }
        }


    }

    fun addGeo(lat: Double, lng: Double, id: String) {
        val startMarker = Marker(binding.map)
        startMarker.setOnMarkerClickListener(object : Marker.OnMarkerClickListener {
            override fun onMarkerClick(marker: Marker?, mapView: MapView?): Boolean {

                val arrayOfLocalImage = viewModel.listImage.value
                arrayOfLocalImage.forEachIndexed { index, localImage ->
                    if (localImage.id == id)
                        binding.rvImages.smoothScrollToPosition(index)
                }
                return true
            }
        })
        startMarker.setPosition(GeoPoint(lat, lng))
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        binding.map.getOverlays().add(startMarker)
    }

    fun createPlanName() {
        val et: EditText = EditText(requireContext())
        SweetAlertDialog(
            requireContext(),
            SweetAlertDialog.CUSTOM_IMAGE_TYPE
        ).setTitleText("Your trip 's name!!")
            .setContentText("Here's a custom image.")
            .setCustomView(et)
            .setCustomImage(R.drawable.ic_launcher_foreground)
            .setConfirmClickListener {
                it.dismiss()
                viewModel.createPlace(LocalPlace(title = et.text.toString(), desc = "......."))
//                Toast.makeText(requireContext(), "OK ${et.text.toString()}", Toast.LENGTH_SHORT)
//                    .show()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun moveToGeo(lat: Double, lng: Double) {
        val startMarker = Marker(binding.map)
        startMarker.setPosition(GeoPoint(lat, lng))
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        binding.map.getOverlays().add(startMarker)
        binding.map.controller.animateTo(GeoPoint(lat, lng))
    }
}