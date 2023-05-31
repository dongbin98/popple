package com.dongbin.popple.ui.gps

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dongbin.popple.R
import com.dongbin.popple.databinding.ActivityGpsBinding
import com.dongbin.popple.databinding.ActivityMainBinding
import com.dongbin.popple.ui.main.MainActivity

class GpsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGpsBinding

    private val locationPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            when {
                permission.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Toast.makeText(this, "위치기반 액세스 권한에 동의하셨습니다.", Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).run {
                        startActivity(this)
                    }
                }

                permission.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Toast.makeText(this, "위치기반 액세스 권한에 동의하셨습니다.", Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).run {
                        startActivity(this)
                    }
                }

                else -> {
                    Toast.makeText(this, "위치기반 액세스 권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                    Intent(this, MainActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        binding.tvGpsDisagree.setOnClickListener {
            Intent(this, MainActivity::class.java).run {
                startActivity(this)
            }
        }

        binding.btGpsAgree.setOnClickListener {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}