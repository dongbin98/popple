package com.dongbin.popple.ui.enroll

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dongbin.popple.ui.enroll.ui.fleemarket.EnrollFleeMarketFragment
import com.dongbin.popple.ui.enroll.ui.popupstore.EnrollPopupStoreFragment

class EnrollAdapter(fa: FragmentActivity, count: Int): FragmentStateAdapter(fa) {

    private val mCount = count

    override fun getItemCount(): Int {
        return mCount
    }

    override fun createFragment(position: Int): Fragment {
        return if(getRealPosition(position) == 0) {
            val fg = EnrollFleeMarketFragment()
            fg
        } else {
            val fg = EnrollPopupStoreFragment()
            fg
        }
    }

    private fun getRealPosition(position: Int) = position % mCount
}