package com.example.lab4mobileapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SecondActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences
    private lateinit var phoneInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Get the email address from the intent
        val email = intent.getStringExtra("EmailAddress")
        val welcomeTextView: TextView = findViewById(R.id.welcomeText)
        welcomeTextView.text = "Welcome back, $email"

        phoneInput = findViewById(R.id.phoneInput)
        prefs = getSharedPreferences("MyData", MODE_PRIVATE)

        // Load saved phone number from SharedPreferences
        val savedPhone = prefs.getString("PhoneNumber", "")
        phoneInput.setText(savedPhone)

        // Handle call button click
        val callButton: Button = findViewById(R.id.callButton)
        callButton.setOnClickListener {
            val phoneNumber = phoneInput.text.toString()

            // Save phone number to SharedPreferences
            val editor = prefs.edit()
            editor.putString("PhoneNumber", phoneNumber)
            editor.apply()

            // Start the phone dialer
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(dialIntent)
        }
    }
}
