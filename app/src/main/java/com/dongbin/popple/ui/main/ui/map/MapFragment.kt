package com.dongbin.popple.ui.main.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.R
import com.dongbin.popple.databinding.FragmentMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.io.IOException
import java.util.Locale

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private val locationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 60000)
            .setIntervalMillis(60000)
            .setMinUpdateIntervalMillis(30000)
            .build()
    }
    private val locationSource by lazy {
        FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }
    private lateinit var naverMap: NaverMap
    private lateinit var myGps: LatLng

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

            val fineLocationGranted = permission[Manifest.permission.ACCESS_FINE_LOCATION]
            val coarseLocationGranted = permission[Manifest.permission.ACCESS_COARSE_LOCATION]

            if (fineLocationGranted != true && coarseLocationGranted != true) {
                Toast.makeText(requireContext(), "위치기반 액세스 권한에 동의하셨습니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "위치기반 액세스 권한에 동의하셨습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    // 위치 갱신 콜백
    private val locationCallBack = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            myGps = LatLng(
                result.lastLocation!!.latitude,
                result.lastLocation!!.longitude
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val mapViewModel =
            ViewModelProvider(this)[MapViewModel::class.java]
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // MapView 세팅
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        init()

        return binding.root
    }

    private fun init() {
        if(checkSelfPermission()) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallBack, Looper.myLooper())
            initView()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    private fun initView() = with(binding) {
        ibGpsFix.setOnClickListener {
            moveToMyGps()
        }
    }

    private fun moveToMyGps() {
        Toast.makeText(requireContext(), "내 위치로 이동", Toast.LENGTH_SHORT).show()
        // 위치 권한 없을 시 내 위치 불러오지 않음
        if (!checkSelfPermission())
            return

        var currentLocation: Location?
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location == null)
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallBack,
                    Looper.myLooper()
                )
            currentLocation = location
            // 위치 오버레이 가시성 default false, true 시 파란색 점으로 현재 위치 표시
            naverMap.locationOverlay.run {
                isVisible = true
                position = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
            }
            // 카메라 현재 위치로 이동
            naverMap.moveCamera(
                CameraUpdate.scrollTo(
                    LatLng(
                        currentLocation!!.latitude,
                        currentLocation!!.longitude
                    )
                ).animate(CameraAnimation.Linear, 1000)
            )
        }
    }

    private fun checkSelfPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 좌표 -> 주소 변환
    private fun getAddress(lat: Double, lng: Double): String {
        val geoCoder = Geocoder(requireContext(), Locale.KOREA)
        lateinit var address: ArrayList<Address>
        var addressResult = "주소를 가져 올 수 없습니다."
        try {
            // 세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            // 한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geoCoder.getFromLocation(lat, lng, 1) {
                    address = it.toCollection(ArrayList<Address>())
                }
            } else {
                address = geoCoder.getFromLocation(lat, lng, 1) as ArrayList<Address>
            }
            if (address.size > 0) {
                // 주소 받아오기
                val currentLocationAddress = address[0].getAddressLine(0)
                    .toString()
                addressResult = currentLocationAddress

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        // 네이버 맵 불러오기 완료 시 콜백
        this.naverMap = naverMap
        naverMap.locationSource = locationSource   // 내장 위치 추적 기능 사용

        // 마커 표시 (네이버맵 현재 가운데에 항상 위치) -> 이 부분에서 DB에서 받아온 위치 기준으로 마커 찍기
        /*val marker = Marker()
        marker.position = LatLng(
            naverMap.cameraPosition.target.latitude,
            naverMap.cameraPosition.target.longitude
        )
        marker.icon = OverlayImage.fromResource(R.drawable.iv_marker_popupstore)
        marker.map = naverMap*/

        // 카메라 움직임 이벤트 리스너 인터페이스
        /*naverMap.addOnCameraChangeListener { _, _ ->
            marker.position = LatLng(
                naverMap.cameraPosition.target.latitude,
                naverMap.cameraPosition.target.longitude
            )
        }*/

        // 카메라 움직임 종료 이벤트 리스너 인터페이스
        /*naverMap.addOnCameraIdleListener {

        }*/

        moveToMyGps()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}