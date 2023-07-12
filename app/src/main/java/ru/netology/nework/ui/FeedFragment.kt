package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.adapter.FragmentPagerAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    lateinit var adapter: FragmentPagerAdapter
    lateinit var binding: FragmentFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentFeedBinding.inflate(layoutInflater)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun init() {
        val viewPager = binding.viewPager
        adapter = FragmentPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.tabs, viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Posts"
                1 -> tab.text = "Events"
            }
        }.attach()
    }
}
