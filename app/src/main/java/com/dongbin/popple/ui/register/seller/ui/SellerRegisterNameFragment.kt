package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.GlobalApplication
import com.dongbin.popple.R
import com.dongbin.popple.data.api.provideSellerApi
import com.dongbin.popple.databinding.FragmentSellerRegisterNameBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity
import com.dongbin.popple.ui.register.seller.SellerRegisterViewModel
import com.dongbin.popple.ui.register.seller.SellerRegisterViewModelFactory
import java.util.regex.Matcher
import java.util.regex.Pattern

class SellerRegisterNameFragment() : Fragment() {

    private var _binding: FragmentSellerRegisterNameBinding? = null
    private val binding get() = _binding!!

    private val regexForName = "^[가-힣A-Za-z0-9]{2,15}"
    private val patternForName: Pattern = Pattern.compile(regexForName)
    private var isAvailableName: Boolean = false
    private var isDuplicatedName: Boolean = true
    private lateinit var viewModel: SellerRegisterViewModel
    private lateinit var pa: SellerRegisterActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSellerRegisterNameBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(
            requireActivity(), SellerRegisterViewModelFactory(provideSellerApi())
        )[SellerRegisterViewModel::class.java]
        initView()

        return binding.root
    }

    private fun initView() = with(binding) {
        pa = (activity as SellerRegisterActivity)

        pa.setSupportActionBar(tbSellerRegisterName)
        pa.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }

        btSellerRegisterName.setOnClickListener {
            val name = etSellerRegisterName.text.toString()
            if (isAvailableName) {
                pa.sellerInfo.id = GlobalApplication.instance.user.id
                pa.sellerInfo.seller_name = name
                pa.binding.sellerRegisterViewpager.currentItem = 2
            } else {
                Toast.makeText(requireContext(), "제대로 입력하셨는지 확인해보세요", Toast.LENGTH_SHORT).show()
            }
        }

        btSellerRegisterNameCheck.setOnClickListener {
            viewModel.checkName(etSellerRegisterName.text.toString())
        }

        etSellerRegisterName.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                val matcherForName: Matcher = patternForName.matcher(text)
                isAvailableName = if (matcherForName.find()) {
                    binding.etSellerRegisterName.setTextColor(requireContext().getColor(R.color.blue))
                    true
                } else {
                    binding.etSellerRegisterName.setTextColor(requireContext().getColor(R.color.red))
                    false
                }
            }
        }

        viewModel.nameResponse.observe(requireActivity()) {// 이름 중복 확인
            // Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            isDuplicatedName = if (it == "available") {
                Toast.makeText(requireContext(), "가입된 아이디입니다.", Toast.LENGTH_SHORT).show()
                true
            } else {
                Toast.makeText(requireContext(), "사용 가능한 아이디입니다.", Toast.LENGTH_SHORT).show()
                binding.btSellerRegisterNameCheck.apply {
                    setBackgroundColor(requireContext().getColor(R.color.blue))
                    text = "사용 가능"
                }
                false
            }
        }
    }
}