package ru.netology.nework.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import ru.netology.nework.BuildConfig
import ru.netology.nework.R
import ru.netology.nework.databinding.FragmentMapBinding

private const val MAP_API_KEY = ""
private lateinit var mapView: MapView

class MapFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("60596e07-86f2-49f9-b9b4-6d62f8011ee0")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMapBinding.inflate(inflater, container, false)
        MapKitFactory.initialize(requireContext())
        val mapKit = MapKitFactory.getInstance()
        mapKit.resetLocationManagerToDefault()


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}