package com.example.mulaimaneh

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(context: Context) {
    private val dataStore = context.dataStore

    suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
        }
    }

    suspend fun savePassword(password: String) {
        dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }

    suspend fun saveGithubUsername(githubUsername: String) {
        dataStore.edit { preferences ->
            preferences[GITHUB_USERNAME_KEY] = githubUsername
        }
    }

    suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = email
        }
    }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
            preferences.remove(PASSWORD_KEY)
            preferences.remove(EMAIL_KEY)
            preferences.remove(GITHUB_USERNAME_KEY)
        }
    }

    val usernameFlow = dataStore.data.map { preferences ->
        preferences[USERNAME_KEY] ?: ""
    }

    val passwordFlow = dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY] ?: ""
    }

    val githubUsernameFlow = dataStore.data.map { preferences ->
        preferences[GITHUB_USERNAME_KEY] ?: ""
    }

    val emailFlow = dataStore.data.map { preferences ->
        preferences[EMAIL_KEY] ?: ""
    }

    suspend fun getUsername(): String {
        return usernameFlow.first()
    }

    suspend fun getPassword(): String {
        return passwordFlow.first()
    }

    suspend fun getEmail(): String {
        return emailFlow.first()
    }

    suspend fun getGithubUsername(): String {
        return githubUsernameFlow.first()
    }

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val GITHUB_USERNAME_KEY = stringPreferencesKey("github_username")
    }
}
