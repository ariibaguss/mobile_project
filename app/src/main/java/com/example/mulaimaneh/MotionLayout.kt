package com.example.mulaimaneh

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mulaimaneh.databinding.MotionLayoutBinding

class MotionLayout : AppCompatActivity() {

    private lateinit var binding: MotionLayoutBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ButtonClicked", MODE_PRIVATE)

        if (sharedPreferences.getBoolean("ButtonClicked", false)) {
            // Jika tombol sudah pernah diklik, langsung menuju LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            binding = MotionLayoutBinding.inflate(layoutInflater)
            supportActionBar?.hide()
            val view = binding.root
            setContentView(view)

            val loginButton = binding.button

            loginButton.setOnClickListener {
                // Simpan status tombol yang telah diklik
                val editor = sharedPreferences.edit()
                editor.putBoolean("ButtonClicked", true)
                editor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
