package com.adhityabagasmiwa.dicodingsubmissionbfaa.data.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.fragment.FollowersFragment
import com.adhityabagasmiwa.dicodingsubmissionbfaa.ui.fragment.FollowingFragment

class SectionPageAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_follower, R.string.tab_following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence =
        mContext.resources.getString(tabTitles[position])

    override fun getCount(): Int = 2
}