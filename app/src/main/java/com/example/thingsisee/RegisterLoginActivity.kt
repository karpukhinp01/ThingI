package com.example.thingsisee

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.thingsisee.databinding.ActivityRegisterLoginBinding

class RegisterLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}