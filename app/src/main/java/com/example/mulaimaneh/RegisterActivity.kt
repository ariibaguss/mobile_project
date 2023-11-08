package com.example.mulaimaneh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mulaimaneh.databinding.ActivityRegisterBinding
import com.example.mulaimaneh.dao.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.btndftr.setOnClickListener {
            val user = binding.user.text.toString()
            val email = binding.email.text.toString()
            val password = binding.pass.text.toString()
            val nim = binding.nim.text.toString()
            val github = binding.gith.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")

            val userObj = User(user, email, password, nim, github)
            database.child(user).setValue(userObj)
                .addOnSuccessListener {
                    binding.user.text.clear()
                    binding.email.text.clear()
                    binding.pass.text.clear()
                    binding.nim.text.clear()
                    binding.gith.text.clear()
                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
                }
        }

        val loginTextView = binding.masuk
        loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            finish()
        }
    }
}