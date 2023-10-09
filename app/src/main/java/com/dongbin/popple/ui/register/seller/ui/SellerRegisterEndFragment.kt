package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dongbin.popple.databinding.FragmentSellerRegisterEndBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity

class SellerRegisterEndFragment() : Fragment() {

    private var _binding: FragmentSellerRegisterEndBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSellerRegisterEndBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() = with(binding){
        (activity as SellerRegisterActivity).setSupportActionBar(tbSellerRegisterEnd)
        (activity as SellerRegisterActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        btSellerRegisterEnd.setOnClickListener {
            (activity as SellerRegisterActivity).finish()
        }
    }
}