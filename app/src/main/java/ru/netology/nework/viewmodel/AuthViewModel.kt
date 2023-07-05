package ru.netology.nework.viewmodel

import androidx.lifecycle.MutableLiveData
import ru.netology.nework.dto.User
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import ru.netology.nework.api.UserApi
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.auth.AuthState
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: AppAuth,
    private val userApi: UserApi
) : ViewModel() {
    val data: LiveData<AuthState> = auth.authStateFlow.asLiveData(Dispatchers.Default)

    val authenticated: Boolean
        get() = auth.authStateFlow.value.id != 0L

    private val _user = MutableLiveData<User?>()

    val user: LiveData<User?>
        get() = _user
}
