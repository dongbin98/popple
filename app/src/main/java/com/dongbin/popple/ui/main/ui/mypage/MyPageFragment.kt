package com.dongbin.popple.ui.main.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.GlobalApplication
import com.dongbin.popple.R
import com.dongbin.popple.data.model.LoggedUser
import com.dongbin.popple.databinding.FragmentMypageBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity

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

        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        if (GlobalApplication.instance.user.isLogged()) {
            user = GlobalApplication.instance.user
        } else {
            user = LoggedUser(null, "로그인 후 다양한 서비스를 이용해보세요.", "비회원 로그인", null, null, null)
            tvMypageLogout.text = "로그인"
        }

        containerSeller.setOnClickListener { // 셀러 등록
            Intent(requireContext(), SellerRegisterActivity::class.java).run {
                startActivity(this)
            }
        }

        containerLogout.setOnClickListener {// 로그아웃, 로그인
            GlobalApplication.instance.user.clearUser()
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}