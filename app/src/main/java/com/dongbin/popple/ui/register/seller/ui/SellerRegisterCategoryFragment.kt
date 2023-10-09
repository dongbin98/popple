package com.dongbin.popple.ui.register.seller.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import com.dongbin.popple.databinding.FragmentSellerRegisterCategoryBinding
import com.dongbin.popple.ui.register.seller.SellerRegisterActivity

class SellerRegisterCategoryFragment() : Fragment() {

    private var _binding: FragmentSellerRegisterCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var pa: SellerRegisterActivity
    private var selectedItemCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSellerRegisterCategoryBinding.inflate(inflater, container, false)
        initView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        selectedItemCount = 0
        pa.sellerInfo.category.clear()

    }

    private fun initView() = with(binding){
        pa = (activity as SellerRegisterActivity)

        pa.setSupportActionBar(tbSellerRegisterCategory)
        pa.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        btSellerRegisterCategory.setOnClickListener {
            if(selectedItemCount > 2) Toast.makeText(requireContext(), "2개까지 선택 가능합니다", Toast.LENGTH_SHORT).show()
            else if(selectedItemCount == 0) Toast.makeText(requireContext(), "1개 이상 선택해주세요", Toast.LENGTH_SHORT).show()
            else {
                if(cbSellerRegisterCategoryFashion.isChecked) pa.sellerInfo.category.add("fashion")
                else if(cbSellerRegisterCategoryAccessory.isChecked) pa.sellerInfo.category.add("accessory")
                else if(cbSellerRegisterCategoryBeauty.isChecked) pa.sellerInfo.category.add("beauty")
                else if(cbSellerRegisterCategoryLiving.isChecked) pa.sellerInfo.category.add("living")
                else if(cbSellerRegisterCategoryStationery.isChecked) pa.sellerInfo.category.add("stationery")
                else if(cbSellerRegisterCategoryFood.isChecked) pa.sellerInfo.category.add("food")
                else if(cbSellerRegisterCategoryPet.isChecked) pa.sellerInfo.category.add("pet")
                else if(cbSellerRegisterCategoryEtc.isChecked) pa.sellerInfo.category.add("etc")
                pa.binding.sellerRegisterViewpager.currentItem = 3
            }
        }

        val listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            when(buttonView) {
                cbSellerRegisterCategoryFashion -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryAccessory -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryBeauty -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryLiving -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryStationery -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryFood -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryPet -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
                cbSellerRegisterCategoryEtc -> {
                    if(isChecked) selectedItemCount++
                    else selectedItemCount--
                }
            }
        }
        cbSellerRegisterCategoryFashion.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryAccessory.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryBeauty.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryLiving.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryStationery.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryFood.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryPet.setOnCheckedChangeListener(listener)
        cbSellerRegisterCategoryEtc.setOnCheckedChangeListener(listener)
    }
}