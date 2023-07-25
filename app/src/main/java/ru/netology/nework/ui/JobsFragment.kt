package ru.netology.nework.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.netology.nework.R
import ru.netology.nework.adapter.JobAdapter
import ru.netology.nework.adapter.JobOnInteractionListener
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.databinding.FragmentJobsBinding
import ru.netology.nework.dto.Job
import ru.netology.nework.repository.JobRepository
import ru.netology.nework.viewmodel.JobViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class JobsFragment : Fragment() {
    @Inject
    lateinit var repository: JobRepository

    @Inject
    lateinit var auth: AppAuth
    private val viewModel: JobViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobsBinding.inflate(inflater, container, false)

        var adapter = JobAdapter(object : JobOnInteractionListener {
            override fun onLinkClick(url: String) {
                super.onLinkClick(url)
            }

            override fun onRemoveJob(job: Job) {
                super.onRemoveJob(job)
            }
        })

        binding.list.adapter = adapter

//        lifecycleScope.launch {
//            filters.filterBy.collectLatest { userId ->
//                if (filterBy == userId) return@collectLatest
//                filterBy = userId
//                setFabAddButtonVisibility(binding.fab)
//                adapter.showEditingMenu = isMyPage()
//                refresh()
//            }
//        }

        viewModel.jobsData.observe(viewLifecycleOwner) { jobList ->
            val list = ArrayList<Job>()
            jobList.forEach { list.add(it) }
            adapter.submitList(jobList)
        }

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swiperefresh.isRefreshing = state.loading
            //TODO Дописать может быть проверку ошибок
//            if (state.needRefresh) {
//                refresh()
//            }
//            if (state.error) {
//                if (dialog)
//            }
//
        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.data.collectLatest { adapter.submitData(it) }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            adapter.loadStateFlow.collectLatest { state ->
//                binding.swiperefresh.isRefreshing =
//                    state.refresh is LoadState.Loading ||
//                            state.prepend is LoadState.Loading ||
//                            state.append is LoadState.Loading
//            }
//        }
//
//        binding.swiperefresh.setOnRefreshListener(adapter::refresh)
//

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newJobFragment)
        }

        return binding.root
    }
}