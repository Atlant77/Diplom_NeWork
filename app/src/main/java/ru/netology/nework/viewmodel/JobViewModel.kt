package ru.netology.nework.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.Job
import ru.netology.nework.model.FeedModelState
import ru.netology.nework.repository.JobRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class JobViewModel @Inject constructor(
    private val repository: JobRepository,
    private val auth: AppAuth,
) : ViewModel() {
    val autorized: Boolean
        get() = auth.authStateFlow.value.token != null

    private val _jobsData: MutableLiveData<List<Job>> = MutableLiveData()

    val jobsData: LiveData<List<Job>>
        get() = _jobsData

    private val _dataState = MutableLiveData<FeedModelState>()

    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getMyJobs() {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                _jobsData.value = sortingList(repository.getMyJobs())
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    private fun sortingList(list: List<Job>?) = list?.sortedByDescending { job -> job.start }

    fun getUserJobs(userId: Long) {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                _jobsData.value = sortingList(repository.getUserJobs(userId))
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun removeMyJobById(id: Long) {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                repository.removeMyJobById(id)
                _jobsData.value = _jobsData.value?.filter { it.id != id }
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun saveMyJob(job: Job) {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                val saveJob = repository.saveMyJob(job)
                if (job.id == 0L) _jobsData.value = sortingList(_jobsData.value?.plus(saveJob)) else
                    _jobsData.value = sortingList(_jobsData.value?.map {
                        if (it.id == saveJob.id) saveJob.copy(
                            name = saveJob.name,
                            position = saveJob.position,
                            start = saveJob.start,
                            finish = saveJob.finish,
                            link = saveJob.link
                        ) else it
                    })
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }
}