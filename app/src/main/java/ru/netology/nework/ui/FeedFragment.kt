package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
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

    private val viewModel: AuthViewModel by viewModels()

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
        val toolBar = binding.toolbar

        val optionsMenu = binding.menuOptions

        optionsMenu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_main)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.signin -> {
                            // TODO: just hardcode it, implementation must be in homework
                            auth.setAuth(5, "x-token")
                            true
                        }

                        R.id.signup -> {
                            // TODO: just hardcode it, implementation must be in homework
                            auth.setAuth(5, "x-token")
                            true
                        }

                        R.id.signout -> {
                            // TODO: just hardcode it, implementation must be in homework
                            auth.removeAuth()
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

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

//addMenuProvider(object : MenuProvider {
//    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.menu_main, menu)
//
//        menu.let {
////                it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
////                it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
//        }
//    }
//
//    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
//        when (menuItem.itemId) {
//            R.id.signin -> {
//                // TODO: just hardcode it, implementation must be in homework
////                    auth.setAuth(5, "x-token")
//                true
//            }
//            R.id.signup -> {
//                // TODO: just hardcode it, implementation must be in homework
////                    auth.setAuth(5, "x-token")
//                true
//            }
//            R.id.signout -> {
//                // TODO: just hardcode it, implementation must be in homework
////                    auth.removeAuth()
//                true
//            }
//            else -> false
//        }
//
//})
//}