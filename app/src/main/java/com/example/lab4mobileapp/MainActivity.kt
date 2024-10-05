package com.example.lab4mobileapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.editTextTextEmailAddress2)
        prefs = getSharedPreferences("MyData", MODE_PRIVATE)

        // Load saved email from SharedPreferences
        val savedEmail = prefs.getString("LoginName", "")
        emailEditText.setText(savedEmail)

        // Handle login button click
        val loginButton: Button = findViewById(R.id.button)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()

            // Save email to SharedPreferences
            val editor = prefs.edit()
            editor.putString("LoginName", email)
            editor.apply()

            // Start SecondActivity and pass the email
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("EmailAddress", email)
            startActivity(intent)
        }
    }
}
