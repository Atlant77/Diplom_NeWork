package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.FragmentPagerAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    lateinit var adapter: FragmentPagerAdapter

    lateinit var binding: FragmentFeedBinding

    private val authViewModel: AuthViewModel by viewModels()

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
        adapter = FragmentPagerAdapter(this, 3)
        viewPager.adapter = adapter
        viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.tabs, viewPager) { tab, pos ->
            when (pos) {
                0 -> {
                    tab.text = getString(R.string.posts)
                    tab.setIcon(R.drawable.baseline_web_stories_24)
                }

                1 -> {
                    tab.text = getString(R.string.events)
                    tab.setIcon(R.drawable.baseline_event_24)
//                    val badge = tab.getOrCreateBadge()
//                    badge.number = 4
                }

                2 -> {
                    tab.text = getString(R.string.jobs)
                    tab.setIcon(R.drawable.baseline_business_center_24)
                }

                3 -> tab.text = "New"
            }
        }.attach()
    }
}