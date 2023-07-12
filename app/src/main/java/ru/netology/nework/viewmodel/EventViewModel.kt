package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Post
import ru.netology.nework.model.FeedModelState
import ru.netology.nework.model.PhotoModel
import ru.netology.nework.repository.EventRepository
import ru.netology.nework.util.SingleLiveEvent
import javax.inject.Inject

//private val empty = Post(
//    id = 0,
//    content = "",
//    authorId = 0,
//    author = "",
//    authorAvatar = "",
//    likedByMe = false,
//    published = " ",
//)

private val noPhoto = PhotoModel()

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
    auth: AppAuth,
) : ViewModel() {
    private val cached = repository
        .data
        .cachedIn(viewModelScope)

    val data: Flow<PagingData<Event>> = auth.authStateFlow
        .flatMapLatest { (myId, _) ->
            cached.map { pagingData ->
                pagingData.map { post ->
                    post.copy(ownedByMe = post.authorId.toLong() == myId)
                }
            }
        }

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    //    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    init {
        loadEvents()
    }

    fun loadEvents() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshEvents() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

//    fun save() {
//        edited.value?.let {
//            viewModelScope.launch {
//                try {
//                    repository.save(
//                        it, _photo.value?.uri?.let { MediaUpload(it.toFile()) }
//                    )
//
//                    _postCreated.value = Unit
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
//        edited.value = empty
//        _photo.value = noPhoto
//    }
//
//    fun edit(post: Post) {
//        edited.value = post
//    }
//
//    fun changeContent(content: String) {
//        val text = content.trim()
//        if (edited.value?.content == text) {
//            return
//        }
//        edited.value = edited.value?.copy(content = text)
//    }

    fun changePhoto(uri: Uri?) {
        _photo.value = PhotoModel(uri)
    }

    fun likeById(id: Long) {
        TODO()
    }

    fun removeById(id: Long) {
        TODO()
    }
}