package com.courtesy.login_spring_example

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.courtesy.login_spring_example.api.DefaultErrorResponse
import com.courtesy.login_spring_example.api.LoginApi
import com.courtesy.login_spring_example.api.RefreshRequest
import com.courtesy.login_spring_example.api.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(application: Application) : ViewModel() {
    private val encryptionManager = EncryptionManagerImpl(application.applicationContext)

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _error = mutableStateOf("")
    val error: State<String> = _error

    private val _userEmail = mutableStateOf("")
    val userEmail: State<String> = _userEmail

    private val _password = mutableStateOf("")
    val password: State<String> = _password

    private var accessToken: String? = null
    private var refreshToken: String? = null

    // Setters
    fun setUserEmail(email: String) {
        _userEmail.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setError(error: String) {
        _error.value = error
    }

    init {
        if (refreshToken == null) {
            refreshToken = encryptionManager.readRefreshToken()
        }
        if (accessToken == null) {
            accessToken = encryptionManager.readAccessToken()
        }
        _isLoggedIn.value = refreshToken != null
    }

    fun createUserWithEmailAndPassword() = viewModelScope.launch {
        _error.value = ""
        val user = createUser()

        try {
            Log.d(TAG, "create user")
            val response = LoginApi.retrofitService.createUser(user)
            if (response.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
            } else {
                errorHandling(response.errorBody()!!)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            _error.value = "Error, try again"
        }
    }


    fun signInWithEmailAndPassword() = viewModelScope.launch {
        _error.value = ""
        val user = createUser()

        try {
            val response = LoginApi.retrofitService.login(user)
            if (response.isSuccessful) {
                Log.d(TAG, "ResponseBody: ${response.body().toString()}")
                val responseBody = response.body()!!
                setLoginStatusAndJwtToken(responseBody.accessToken, responseBody.refreshToken)
                Log.d(TAG, "loginUserWithEmail:success")
            } else {
                errorHandling(response.errorBody()!!)
            }
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
            Log.d(TAG, "Sign in fail: $e")
        }
    }

    private fun setLoginStatusAndJwtToken(accessToken: String?, refreshToken: String?) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        _isLoggedIn.value = refreshToken != null
        encryptionManager.saveOrRemoveTokens(accessToken, refreshToken)
    }


    private fun createUser(): User {
        return User(email = userEmail.value, password = password.value)
    }

    fun isValidEmailAndPassword(): Boolean {
        if (userEmail.value.isBlank() || password.value.isBlank()) {
            return false
        }
        return true
    }

    fun getUser() = viewModelScope.launch {
        try {
            getUserWithRetry()
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
            Log.d(TAG, "Exception: $e")
        }
    }

    private suspend fun getUserWithRetry(isRetry: Boolean = false) {
        val response = LoginApi.retrofitService.getUser("Bearer $accessToken")
        Log.d(TAG, "ResponseCode: ${response.code()}")
        if (response.isSuccessful) {
            Log.d(TAG, "ResponseBody: ${response.body().toString()}")
        } else if (response.code() == 403 && !isRetry) {
            getNewAccessToken().join()
            getUserWithRetry(true)
        } else {
            errorHandling(response.errorBody()!!)
        }
    }

    private fun getNewAccessToken() = viewModelScope.launch {
        try {
            val refresh = RefreshRequest(refreshToken!!)
            val response = LoginApi.retrofitService.getNewAccessToken(refresh)
            if (response.isSuccessful) {
                accessToken = response.body()!!.accessToken
                encryptionManager.saveNewAccessToken(accessToken!!)
            }
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
            Log.d(TAG, "Exception: $e")
        }
    }

    fun signOut() = viewModelScope.launch {
        try {
            LoginApi.retrofitService.logout("Bearer $accessToken")
            _isLoggedIn.value = false
            accessToken = null
            refreshToken = null
            encryptionManager.saveOrRemoveTokens(null, null)
        } catch (e: Exception) {
            _error.value = e.localizedMessage ?: "Unknown error"
            Log.d(TAG, "Exception: $e")
        }
    }

    private fun errorHandling(responseBody: ResponseBody) {
        try {
            val responseString = responseBody.string()
            Log.d(TAG, "ResponseString: $responseString")

            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val jsonAdapter: JsonAdapter<DefaultErrorResponse> = moshi.adapter(DefaultErrorResponse::class.java)

            val errorResponse = jsonAdapter.fromJson(responseString)
            _error.value = errorResponse?.message ?: "Unknown error!"
            Log.d(TAG, "User created err2: ${error.value}")
        } catch (e: Exception) {
            Log.d(TAG, "ERROR: $e")
            _error.value = "Error, try again"
        }
    }

}