package com.sizdev.arkhirefortalent.homepage.item.account.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sizdev.arkhirefortalent.homepage.item.account.profile.portfolio.PortofolioFragment
import com.sizdev.arkhirefortalent.homepage.item.account.profile.workexperience.WorkExperienceFragment

class TalentProfileTabAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    val fragment = arrayOf(PortofolioFragment(), WorkExperienceFragment())

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Portofolio"
            1 -> "Work Experience"
            else -> ""
        }
    }
}