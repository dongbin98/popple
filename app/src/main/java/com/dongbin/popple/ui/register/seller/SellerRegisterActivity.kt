package com.dongbin.popple.ui.register.seller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.data.api.provideSellerApi
import com.dongbin.popple.data.model.SellerInfo
import com.dongbin.popple.databinding.ActivitySellerRegisterBinding

class SellerRegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivitySellerRegisterBinding
    lateinit var adapter: SellerRegisterAdapter
    lateinit var viewModel: SellerRegisterViewModel

    var sellerInfo = SellerInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SellerRegisterViewModelFactory(provideSellerApi()))[SellerRegisterViewModel::class.java]

        initView()
    }

    private fun initView() = with(binding) {
        // ViewPager2
        adapter =  SellerRegisterAdapter(this@SellerRegisterActivity, 6)
        sellerRegisterViewpager.apply {
            this.adapter = this@SellerRegisterActivity.adapter
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}