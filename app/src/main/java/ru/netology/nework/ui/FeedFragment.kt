package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nework.R
import ru.netology.nework.adapter.FragmentPagerAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentFeedBinding
import ru.netology.nework.util.AndroidUtils
import ru.netology.nework.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var auth: AppAuth

    lateinit var adapter: FragmentPagerAdapter

    lateinit var binding: FragmentFeedBinding

    private val viewModel: AuthViewModel by viewModels()

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
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.subtitle = "This is my program"
        return binding.root
    }

    private fun init() {
        val viewPager = binding.viewPager
        adapter = FragmentPagerAdapter(this, 3)
        viewPager.adapter = adapter
        viewPager.isSaveEnabled = false

        TabLayoutMediator(binding.tabs, viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.text = "Posts"
                1 -> tab.text = "Events"
                2 -> tab.text = "Jobs"
                3 -> tab.text = "New"
            }
        }.attach()
    }
}