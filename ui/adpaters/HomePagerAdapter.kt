package com.qceccenter.qcec.ui.adpaters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private lateinit var mFragList: MutableList<Fragment>;

    fun setFragList(fList: MutableList<Fragment>){
        this.mFragList = fList;
    }


    override fun getItemCount(): Int {
        return mFragList.size;
    }

    override fun createFragment(position: Int): Fragment {
//        when (position) {
//            0 -> return ProfileFragment()
//            1 -> return TransactionsFragment()
//            2 -> return NotificationsFragment()
//            else -> return SettingsFragment()
//        }
        return mFragList.get(position);
    }
}