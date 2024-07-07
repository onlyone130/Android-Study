package com.example.gpsmap

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.gpsmap.databinding.ActivityMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    // PolyLine 옵션 ①
    private val polylineOptions = PolylineOptions().width(5f).color(Color.RED)

    // 위치 정보를 얻기위한 객체 ①
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // 위치 요청 정보 ②
    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()
    }

    // 위치 갱신 요청 시 호출되는 콜백 객체 ③
    private val locationCallback = MyLocationCallback()

    // 권한 요청 런처 ④
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startLocationUpdates()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // 권한 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            showPermissionInfoDialog()
        } else {
            startLocationUpdates()
        }
    }

    // 위치 업데이트 시작 ⑤
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // 위치 업데이트 중지 ⑥
    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun showPermissionInfoDialog() {
        // 다이얼로그에 권한이 필요한 이유를 설명
        AlertDialog.Builder(this).apply {   // ④
            setTitle("권한이 필요한 이유")
            setMessage("지도에 위치를 표시하려면 위치 정보 권한이 필요합니다.")
            setPositiveButton("권한 요청") { _, _ ->    // ⑤
                // 권한 요청
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)  // ⑥
            }
            setNegativeButton("거부", null)   // ⑦
        }.show()    // ⑧
    }

    /**
     * 사용 가능한 맵을 조작합니다.
     * 지도를 사용할 준비가 되면 이 콜백이 호출됩니다.
     * 여기서 마커나 선, 청취자를 추가하거나 카메라를 이동할 수 있습니다.
     * 호주 시드니 근처에 마커 추가하고 있습니다.
     * Google Play 서비스가 기기에 설치되어 있지 않은 경우 사용자에게
     * SupportMapFragment 안에 Google Play서비스를 설치하라는 메시지가
     * 표시됩니다. 이 메서드는 사용자가 Google Play 서비스를 설치하고 앱으로
     * 돌아온 후에만 호출(혹은 실행)됩니다.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap    // ②

        // 시드니에 마커를 추가하고 카메라를 이동합니다 ③
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    // ⑥
    inner class MyLocationCallback : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)

            val location = locationResult.lastLocation

            // ⑦
            location?.run {
                // 17 level로 확대하며 현재 위치로 카메라 이동
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))

                Log.d("MapsActivity", "위도: $latitude, 경도: $longitude") // ①

                // PolyLine에 좌표 추가 ②
                polylineOptions.add(latLng)

                // 선 그리기 ③
                mMap.addPolyline(polylineOptions)
            }

        }
    }

    // 액티비티가 재개될 때 위치 업데이트 시작
    override fun onResume() {
        super.onResume()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
        }
    }

    // 액티비티가 일시정지될 때 위치 업데이트 중지
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}
