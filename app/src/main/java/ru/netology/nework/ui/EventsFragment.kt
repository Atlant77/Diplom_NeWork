package ru.netology.nework.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nework.R
import ru.netology.nework.adapter.EventOnInteractionListener
import ru.netology.nework.adapter.EventsAdapter
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentEventsBinding
import ru.netology.nework.dto.Event
import ru.netology.nework.repository.EventRepository
import ru.netology.nework.viewmodel.EventViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment() {
    @Inject
    lateinit var repository: EventRepository

    @Inject
    lateinit var auth: AppAuth
    private val viewModel: EventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventsBinding.inflate(inflater, container, false)

        val adapter = EventsAdapter(object : EventOnInteractionListener {
            override fun onEdit(event: Event) {
//                viewModel.edit(event)
            }

            override fun onLike(event: Event) {
//                viewModel.likeById(event.id)
            }

            override fun onRemove(event: Event) {
//                viewModel.removeById(event.id)
            }

            override fun onShare(event: Event) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.chooser_share_event))
                startActivity(shareIntent)
            }
        })
        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest { adapter.submitData(it) }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { state ->
                binding.swiperefresh.isRefreshing =
                    state.refresh is LoadState.Loading ||
                            state.prepend is LoadState.Loading ||
                            state.append is LoadState.Loading
            }
        }

        binding.swiperefresh.setOnRefreshListener(adapter::refresh)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}