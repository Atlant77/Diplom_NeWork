package ru.netology.nework.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val auth: AppAuth,
    private val repository: AuthRepository,
) : ViewModel() {
    fun logIn(login: String, password: String) = viewModelScope.launch {
        val response = repository.logIn(login, password)
        response.token?.let {
            auth.setAuth(response.id, response.token)
        }
    }
}