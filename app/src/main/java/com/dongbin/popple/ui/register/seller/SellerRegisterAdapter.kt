package com.dongbin.popple.ui.register.seller

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterCategoryFragment
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterChannelFragment
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterEndFragment
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterNameFragment
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterProfileFragment
import com.dongbin.popple.ui.register.seller.ui.SellerRegisterStartFragment

class SellerRegisterAdapter(fa: FragmentActivity, count: Int): FragmentStateAdapter(fa) {

    private val mCount = count

    override fun getItemCount(): Int {
        return mCount
    }

    override fun createFragment(position: Int): Fragment {
        return when(getRealPosition(position)) {
            0 -> {
                SellerRegisterStartFragment()
            }
            1 -> {
                SellerRegisterNameFragment()
            }
            2 -> {
                SellerRegisterCategoryFragment()
            }
            3 -> {
                SellerRegisterProfileFragment()
            }
            4 -> {
                SellerRegisterChannelFragment()
            }
            else -> {
                SellerRegisterEndFragment()
            }
        }
    }

    private fun getRealPosition(position: Int) = position % mCount
}