package ru.netology.nework.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.ui.EventsFragment
import ru.netology.nework.ui.FeedFragment
import ru.netology.nework.ui.JobsFragment
import ru.netology.nework.ui.PostsFragment

class FragmentPagerAdapter(
    fragmentActivity: FeedFragment,
    var totalTabs: Int,
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return totalTabs
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PostsFragment()
            1 -> EventsFragment()
            2 -> JobsFragment()
            else -> createFragment(position)
        }
    }
}