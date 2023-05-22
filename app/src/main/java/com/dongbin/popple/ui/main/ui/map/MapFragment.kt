package com.dongbin.popple.ui.main.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.R
import com.dongbin.popple.databinding.FragmentMapBinding
import com.naver.maps.map.MapFragment

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        val mapViewModel =
            ViewModelProvider(this)[MapViewModel::class.java]

        _binding = FragmentMapBinding.inflate(inflater, container, false)

        val cm = childFragmentManager
        val mapFragment = cm.findFragmentById(R.id.map_view) as MapFragment?
            ?: MapFragment.newInstance().also {
                cm.beginTransaction().add(R.id.map_view, it).commit()
            }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}