package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dongbin.popple.R
import com.dongbin.popple.databinding.FragmentSellerRegisterProfileBinding
import com.dongbin.popple.databinding.FragmentSellerRegisterStartBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity

class SellerRegisterProfileFragment() : Fragment() {

    private var _binding: FragmentSellerRegisterProfileBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSellerRegisterProfileBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() = with(binding){
        (activity as SellerRegisterActivity).setSupportActionBar(tbSellerRegisterProfile)
        (activity as SellerRegisterActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        btSellerRegisterProfile.setOnClickListener {
            (activity as SellerRegisterActivity).binding.sellerRegisterViewpager.currentItem = 4
        }
    }
}