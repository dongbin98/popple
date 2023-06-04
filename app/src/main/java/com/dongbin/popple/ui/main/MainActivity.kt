package com.dongbin.popple.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dongbin.popple.R
import com.dongbin.popple.databinding.ActivityMainBinding
import com.dongbin.popple.ui.enroll.EnrollActivity
import com.dongbin.popple.ui.main.ui.heart.HeartFragment
import com.dongbin.popple.ui.main.ui.map.MapFragment
import com.dongbin.popple.ui.main.ui.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // 3 Fragment for BottomNavigation
    private var mapFragment: MapFragment? = null
    private var heartFragment: HeartFragment? = null
    private var myPageFragment: MyPageFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("MainActivity", "onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        initBottomNavigation()

        // AAC Navigation Replace 이슈로 사용X
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        binding.bottomNav.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.registrationFragment -> {
//                    Toast.makeText(this, "등록하기 클릭", Toast.LENGTH_SHORT).show()
//                }
//            }
//            false
//        }
//
//        binding.bottomNav.setupWithNavController(navController)
    }

    private fun initBottomNavigation() = with(binding) {

        mapFragment = MapFragment()
        supportFragmentManager.beginTransaction().replace(container.id, mapFragment!!)
            .commit()

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_map -> {
                    if (mapFragment == null) {
                        mapFragment = MapFragment()
                        supportFragmentManager.beginTransaction().add(
                            container.id,
                            mapFragment!!
                        ).commit()
                    }
                    if (mapFragment != null) supportFragmentManager.beginTransaction()
                        .show(mapFragment!!).commit()
                    if (heartFragment != null) supportFragmentManager.beginTransaction()
                        .hide(heartFragment!!).commit()
                    if (myPageFragment != null) supportFragmentManager.beginTransaction()
                        .hide(myPageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }

                R.id.menu_heart -> {
                    if (heartFragment == null) {
                        heartFragment = HeartFragment()
                        supportFragmentManager.beginTransaction().add(
                            binding.container.id,
                            heartFragment!!
                        ).commit()
                    }
                    if (heartFragment != null) supportFragmentManager.beginTransaction()
                        .show(heartFragment!!).commit()
                    if (mapFragment != null) supportFragmentManager.beginTransaction()
                        .hide(mapFragment!!).commit()
                    if (myPageFragment != null) supportFragmentManager.beginTransaction()
                        .hide(myPageFragment!!).commit()

                    return@setOnItemSelectedListener true
                }

                R.id.menu_assign -> {
                    // 클릭 이전 프래그먼트 위치로 가도록 해야함
                    for (fragment in supportFragmentManager.fragments) {
                        when (fragment) {
                            is MapFragment -> {
                                supportFragmentManager.beginTransaction()
                                    .show(mapFragment!!).commit()
                                bottomNav.selectedItemId = R.id.menu_map
                                Log.i("BottomNavigation", "이전 프래그먼트 : 맵")
                            }

                            is HeartFragment -> {
                                supportFragmentManager.beginTransaction()
                                    .show(heartFragment!!).commit()
                                bottomNav.selectedItemId = R.id.menu_heart
                                Log.i("BottomNavigation", "이전 프래그먼트 : 찜")
                            }

                            is MyPageFragment -> {
                                supportFragmentManager.beginTransaction()
                                    .show(myPageFragment!!).commit()
                                bottomNav.selectedItemId = R.id.menu_mypage
                                Log.i("BottomNavigation", "이전 프래그먼트 : ")
                            }
                        }
                        break
                    }
                    Intent(this@MainActivity, EnrollActivity::class.java).run {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.menu_mypage -> {
                    if (myPageFragment == null) {
                        myPageFragment = MyPageFragment()
                        supportFragmentManager.beginTransaction().add(
                            binding.container.id,
                            myPageFragment!!
                        ).commit()
                    }
                    if (myPageFragment != null) supportFragmentManager.beginTransaction()
                        .show(myPageFragment!!).commit()
                    if (heartFragment != null) supportFragmentManager.beginTransaction()
                        .hide(heartFragment!!).commit()
                    if (mapFragment != null) supportFragmentManager.beginTransaction()
                        .hide(mapFragment!!).commit()

                    return@setOnItemSelectedListener true
                }

                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }
}