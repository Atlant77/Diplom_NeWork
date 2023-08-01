package ru.netology.nework.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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

    @SuppressLint("CheckResult")
    private fun init() {
        val viewPager = binding.viewPager
        adapter = FragmentPagerAdapter(this, 3)
        viewPager.adapter = adapter
        viewPager.isSaveEnabled = false

        val toolBar = binding.topAppBar

        toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.log_in -> {
                    findNavController().navigate(R.id.action_feedFragment_to_logInFragment)
                    true
                }

                R.id.registr -> {
                    findNavController().navigate(R.id.action_feedFragment_to_registrationFragment)
                    true
                }

                R.id.logout -> {
                    auth.removeAuth()
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }

        toolBar.setNavigationOnClickListener {
            Toast.makeText(
                activity,
                "Make here bottom sheet",
                Toast.LENGTH_LONG
            ).show()

        }

        authViewModel.data.observe(this) {
            toolBar.menu.let {
                it.setGroupVisible(R.id.unauthenticated, !authViewModel.authorized)
                it.setGroupVisible(R.id.authenticated, authViewModel.authorized)
            }
            if (authViewModel.authorized) {
                binding.topAppBar.let {
                    it.title = authViewModel.authUser.value?.name
                }

                Glide.with(this)
                    .asDrawable()
                    .load(authViewModel.authUser.value?.avatar)
                    .placeholder(R.drawable.baseline_downloading_24)
                    .error(R.drawable.baseline_broken_image_24)
                    .timeout(10_000)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            toolBar.setLogo(resource)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }
                    })
            } else {
                binding.topAppBar.let {
                    it.title = "Not login"
                    it.setLogo(R.drawable.baseline_account_circle_24)
                }
            }
        }



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