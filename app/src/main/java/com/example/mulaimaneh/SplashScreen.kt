package com.example.mulaimaneh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.mulaimaneh.UserPreferences

class SplashScreen : AppCompatActivity() {
    private val userPreferences by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            checkLoginStatus()
        }, 3000)
    }

    private fun checkLoginStatus() {
        GlobalScope.launch(Dispatchers.IO) {
            val username = userPreferences.getUsername()
            val password = userPreferences.getPassword()

            launch(Dispatchers.Main) {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    // Pengguna masih login, pergi ke MainActivity
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Pengguna belum login, pergi ke LoginActivity
                    val intent = Intent(this@SplashScreen, MotionLayout::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }
    }
}
