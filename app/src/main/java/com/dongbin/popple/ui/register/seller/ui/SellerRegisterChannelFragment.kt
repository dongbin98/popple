package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.R
import com.dongbin.popple.data.api.provideSellerApi
import com.dongbin.popple.data.dto.seller.SellerRegisterRequestDto
import com.dongbin.popple.databinding.FragmentSellerRegisterChannelBinding
import com.dongbin.popple.databinding.FragmentSellerRegisterStartBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity
import com.dongbin.popple.ui.register.seller.SellerRegisterViewModel
import com.dongbin.popple.ui.register.seller.SellerRegisterViewModelFactory

class SellerRegisterChannelFragment() : Fragment() {

    private var _binding: FragmentSellerRegisterChannelBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SellerRegisterViewModel
    private lateinit var pa: SellerRegisterActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSellerRegisterChannelBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(), SellerRegisterViewModelFactory(
                provideSellerApi()
            )
        )[SellerRegisterViewModel::class.java]
        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        pa = (activity as SellerRegisterActivity)

        pa.setSupportActionBar(tbSellerRegisterChannel)
        pa.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        btSellerRegisterChannel.setOnClickListener {
            if (etSellerRegisterChannelInstagram.text.isNotEmpty()) {
                pa.sellerInfo.instaAccount = etSellerRegisterChannelInstagram.text.toString()
            }
            if (etSellerRegisterChannelYoutube.text.isNotEmpty()) {
                pa.sellerInfo.youtubeAccount = etSellerRegisterChannelYoutube.text.toString()
            }
            if (etSellerRegisterChannelWebsite.text.isNotEmpty()) {
                pa.sellerInfo.websiteUrl = etSellerRegisterChannelWebsite.text.toString()
            }
            viewModel.register(
                SellerRegisterRequestDto(
                    id = pa.sellerInfo.id!!,
                    seller_name = pa.sellerInfo.seller_name.toString(),
                    seller_category = pa.sellerInfo.category,
                    insta_account = pa.sellerInfo.instaAccount,
                    youtube_account = pa.sellerInfo.youtubeAccount,
                    website_url = pa.sellerInfo.websiteUrl,
                    seller_profile_image = pa.sellerInfo.sellerProfileImage
                )
            )
            (activity as SellerRegisterActivity).binding.sellerRegisterViewpager.currentItem = 5
        }

        viewModel.registerResponse.observe(requireActivity()) {
            if (it == pa.sellerInfo.seller_name.toString()) {
                (activity as SellerRegisterActivity).binding.sellerRegisterViewpager.currentItem = 5
            } else {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}