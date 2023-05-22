package com.dongbin.popple.ui.gps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.dongbin.popple.R
import com.dongbin.popple.databinding.ActivityGpsBinding
import com.dongbin.popple.databinding.ActivityMainBinding

class GpsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGpsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

    }
}