package com.dongbin.popple.ui.enroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.dongbin.popple.databinding.ActivityEnrollBinding

class EnrollActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        // Toolbar
        setSupportActionBar(tbEnroll)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        // ViewPager2
        val adapter =  EnrollAdapter(this@EnrollActivity, 2)
        enrollViewpager.apply {
            this.adapter = adapter
            isUserInputEnabled = false
        }

        // RadioButton
        rgEnroll.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                rbFleeMarket.id -> enrollViewpager.currentItem = 0
                else -> enrollViewpager.currentItem = 1
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}