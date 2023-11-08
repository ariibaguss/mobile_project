package com.example.mulaimaneh

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mulaimaneh.dao.User
import com.example.mulaimaneh.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var username: EditText
    private lateinit var pass: EditText
    private lateinit var button: Button
    private lateinit var database: DatabaseReference
    private val userPreferences by lazy { UserPreferences(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        username = binding.username
        pass = binding.pass
        button = binding.button
        database = FirebaseDatabase.getInstance().reference.child("Users")

        button.setOnClickListener {
            if (checkInputs()) {
                val enteredUsername = username.text.toString()
                val enteredPassword = pass.text.toString()

                // Retrieve user data from Firebase Database based on username
                database.orderByChild("user").equalTo(enteredUsername)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (userSnapshot in dataSnapshot.children) {
                                    val user = userSnapshot.getValue(User::class.java)

                                    // Check if the password matches
                                    if (user?.password == enteredPassword) {
                                        val userUsername = user?.user
                                        val githubUsername = user?.github
                                        val userEmail = user?.email

                                        Toast.makeText(this@LoginActivity, "Welcome, $userUsername", Toast.LENGTH_LONG).show()

                                        // Simpan username dan githubUsername
                                        MainScope().launch {
                                            userPreferences.saveUsername(enteredUsername)
                                            userPreferences.savePassword(enteredPassword)
                                            userPreferences.saveEmail(userEmail.toString())
                                            userPreferences.saveGithubUsername(githubUsername.toString())
                                        }

                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)

                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Incorrect password
                                        Toast.makeText(this@LoginActivity, "Wrong Password", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } else {
                                // Username not found
                                Toast.makeText(this@LoginActivity, "Username not found", Toast.LENGTH_LONG).show()
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle errors
                            Toast.makeText(this@LoginActivity, "Error: " + databaseError.message, Toast.LENGTH_LONG).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Enter the Details", Toast.LENGTH_LONG).show()
            }
        }

        val registerTextView = binding.daftar
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }

    private fun checkInputs(): Boolean {
        if (username.text.toString().trim().isNotEmpty()
            && pass.text.toString().trim().isNotEmpty()
        ) {
            return true
        }
        return false
    }
}
