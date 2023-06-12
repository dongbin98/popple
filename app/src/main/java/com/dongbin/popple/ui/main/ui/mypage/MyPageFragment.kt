package com.dongbin.popple.ui.main.ui.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.GlobalApplication
import com.dongbin.popple.R
import com.dongbin.popple.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {

    private var _binding: FragmentMypageBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val myPageViewModel =
            ViewModelProvider(this)[MyPageViewModel::class.java]

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mypage, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.user = GlobalApplication.instance

        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        tvMypageLogout.setOnClickListener {
            GlobalApplication.instance.clearUserInfo()
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}