package ru.netology.nework.auth

import android.content.Context
import android.content.SharedPreferences
import android.system.Os.remove
import android.widget.Toast
import androidx.core.content.edit
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nework.R
import ru.netology.nework.api.UserApi
import ru.netology.nework.dto.PushToken
import ru.netology.nework.dto.Token
import ru.netology.nework.error.ApiError
import ru.netology.nework.error.NetworkError
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    companion object {
        private const val ID_KEY = "ID_KEY"
        private const val TOKEN_KEY = "TOKEN_KEY"
    }

    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(ID_KEY, 0)
        val token = prefs.getString(TOKEN_KEY, null)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            removeAuth()
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String) {
        _authStateFlow.value = AuthState(id, token)
        prefs.edit {
            putLong(ID_KEY, id)
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getAuthorizedUserId(): Long = _authStateFlow.value.id ?: 0L

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        prefs.edit {
            remove(TOKEN_KEY)
            remove(ID_KEY)
            apply()
        }
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
//                getApiService(context).pushTokens(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getApiService(context: Context): UserApi {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(
            context,
            AppAuthEntryPoint::class.java
        )
        return hiltEntryPoint.apiService()
    }

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface AppAuthEntryPoint {
        fun apiService(): UserApi
    }
}

data class AuthState(val id: Long = 0, val token: String? = null)