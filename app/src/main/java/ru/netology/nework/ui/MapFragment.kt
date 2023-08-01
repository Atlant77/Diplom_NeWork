package ru.netology.nework.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import ru.netology.nework.BuildConfig
import ru.netology.nework.R
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentMapBinding
import ru.netology.nework.dto.Coordinates
import javax.inject.Inject

class MapFragment : Fragment(R.layout.fragment_map), InputListener {

    private var mapView: MapView? = null
    private lateinit var mapObjects: MapObjectCollection

    private val args: MapFragmentArgs by navArgs()
    private var coordinates: Coordinates? = null
    private var readOnly: Boolean = false

    @Inject
    lateinit var appAuth: AppAuth

    private lateinit var userLocation: UserLocationLayer

    override fun onCreate(savedInstanceState: Bundle?) {
        context?.let { MapKitInitializer.initialize(BuildConfig.MAPS_API_KEY, it) }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMapBinding.inflate(inflater, container, false)

        mapView = binding.mapView

        coordinates = args.coordinatesArgs
        readOnly = args.readOnly

        MapKitFactory.initialize(requireContext())
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()

        if (readOnly) {
            binding.getMyLocation.setOnClickListener {
                binding.getMyLocation.visibility = View.VISIBLE
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        } else {
            binding.getMyLocation.visibility = View.GONE
        }

        mapView?.getMap()?.move(
            CameraPosition(
                Point(coordinates!!.lat.toDouble(), coordinates!!.long.toDouble()),
                14.0f, 0.0f, 0.0f
            ),
            Animation(
                Animation.Type.SMOOTH,
                5F
            ),
            null
        )

        // Inflate the layout for this fragment
        return binding.root
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    userLocation.isVisible = true
                    userLocation.isHeadingEnabled = false
                    userLocation.cameraPosition()?.target?.apply {
                        val map = mapView?.map ?: return@registerForActivityResult
                        val cameraPosition = map.cameraPosition
                        map.move(
                            CameraPosition(
                                this,
                                cameraPosition.zoom,
                                cameraPosition.azimuth,
                                cameraPosition.tilt,
                            ), Animation(Animation.Type.SMOOTH, 5F),
                            null
                        )
                    }
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        "Location permission required",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        mapView?.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView = null
    }

    override fun onMapTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }

    override fun onMapLongTap(p0: Map, p1: Point) {
        TODO("Not yet implemented")
    }
}


// This object for safety initializing MapKit inside this fragment (not in Application)
object MapKitInitializer {
    private var initialized = false

    fun initialize(apiKey: String, context: Context) {
        if (initialized) {
            return
        }

        MapKitFactory.setApiKey(apiKey)
        MapKitFactory.initialize(context)
        initialized = true
    }
}