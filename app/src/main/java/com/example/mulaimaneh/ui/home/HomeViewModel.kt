package com.example.mulaimaneh.ui.home

import androidx.lifecycle.*
import com.example.mulaimaneh.dao.SettingPreferences
import com.example.mulaimaneh.network.ApiConfig
import com.example.mulaimaneh.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val resultUser = MutableLiveData<Result>()

    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .getUserGithub()

                emit(response)
            }.onStart {
                resultUser.value = Result.Loading(false)
            }.onCompletion {
                resultUser.value = Result.Loading(false)
            }.catch { error ->
                error.printStackTrace()
                resultUser.value = Result.Error(error)
            }.collect {
                resultUser.value = Result.Success(it)
            }
        }
    }

    fun getUser(query: String) {
        viewModelScope.launch {
            flow {
                val response = ApiConfig
                    .githubService
                    .searchUserGithub(
                        mapOf(
                            "q" to "in:header $query",
                            "per_page" to 10
                        )
                    )
                emit(response)
            }.onStart {
                resultUser.value = Result.Loading(false)
            }.onCompletion {
                resultUser.value = Result.Loading(false)
            }.catch { error ->
                error.printStackTrace()
                resultUser.value = Result.Error(error)
            }.collect {
                resultUser.value = Result.Success(it.items)
            }
        }
    }

    class Factory(private val preferences: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(preferences) as T
    }
}