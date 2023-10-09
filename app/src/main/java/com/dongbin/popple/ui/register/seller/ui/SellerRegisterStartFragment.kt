package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dongbin.popple.databinding.FragmentSellerRegisterStartBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity

class SellerRegisterStartFragment : Fragment() {

    private var _binding: FragmentSellerRegisterStartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSellerRegisterStartBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        (activity as SellerRegisterActivity).setSupportActionBar(tbSellerRegisterStart)
        (activity as SellerRegisterActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        btSellerRegisterStart.setOnClickListener {
            (activity as SellerRegisterActivity).binding.sellerRegisterViewpager.currentItem = 1
        }
    }
}