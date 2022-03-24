package edu.xww.urchat.adapter

import android.util.SparseArray
import androidx.core.util.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPager2Adapter(list: SparseArray<Fragment>, fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var fragments = list

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }


}