package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.auth.AuthState
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.Token
import ru.netology.nework.dto.User
import ru.netology.nework.enumeration.AttachmentType
import ru.netology.nework.error.NetworkError
import ru.netology.nework.error.UnknownError
import ru.netology.nework.model.FeedModelState
import ru.netology.nework.model.MediaModel
import ru.netology.nework.repository.AuthRepository
import ru.netology.nework.repository.UserRepository
import java.io.File
import java.io.IOException
import javax.inject.Inject

private val noAvatar = MediaModel()
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    val data: LiveData<AuthState> = appAuth.authStateFlow.asLiveData(Dispatchers.Default)

    private val _avatar = MutableLiveData(noAvatar)
    val avatar: LiveData<MediaModel>
        get() = _avatar

    private val _authorizationData: MutableLiveData<Token?> = MutableLiveData(null)
    val authorizationData: LiveData<Token?>
        get() = _authorizationData

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState
    val authorized: Boolean
        get() = appAuth.authStateFlow.value.token != null

    private val _authUser: MutableLiveData<User?> = MutableLiveData(null)
    val authUser: LiveData<User?>
        get() = _authUser

    fun clearAuthUser() {
        _authUser.value = null
    }

    fun getUserById(id: Long): User? {
        viewModelScope.launch {
            if (id == 0L) {
                _authUser.value = null
            } else {
                try {
                    _dataState.value = FeedModelState(loading = true)
                    _authUser.value = userRepository.getUserById(id)
                    _dataState.value = FeedModelState()
                    return@launch
                } catch (e: IOException) {
                    throw NetworkError
                } catch (e: Exception) {
                    throw UnknownError
                }
            }
        }
        return null
    }

    fun authorization(login: String, pass: String) {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                _authorizationData.value = authRepository.authentication(login, pass)
                _dataState.value = FeedModelState()
            } catch (e: IOException) {
                _authorizationData.value = null
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun registration(login: String, pass: String, name: String) {
        viewModelScope.launch {
            try {
                _dataState.value = FeedModelState(loading = true)
                when (_avatar.value) {
                    noAvatar -> _authorizationData.value =
                        authRepository.registration(login, pass, name)

                    else -> _avatar.value?.file.let {
                        _authorizationData.value = authRepository.registrationWithPhoto(
                            login.toRequestBody("text/plain".toMediaType()),
                            pass.toRequestBody("text/plain".toMediaType()),
                            name.toRequestBody("text/plain".toMediaType()),
                            MediaUpload(it!!)
                        )
                    }
                }
                _avatar.value = noAvatar
                _dataState.value = FeedModelState()
            } catch (e: IOException) {
                _authorizationData.value = null
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun changeAvatar(uri: Uri?, file: File?) {
        _avatar.value = MediaModel(uri, file, AttachmentType.IMAGE)
    }
}
