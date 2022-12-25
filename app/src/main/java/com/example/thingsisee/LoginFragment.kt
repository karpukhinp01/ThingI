package com.example.thingsisee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.ViewModels.RegisterLoginViewModel
import com.example.thingsisee.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mRegisterLoginViewModel: RegisterLoginViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mRegisterLoginViewModel = ViewModelProvider(this).get(RegisterLoginViewModel::class.java)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val email= binding.email
        val password= binding.password


        binding.registerButton.setOnClickListener { findNavController()
            .navigate(R.id.action_loginFragment_to_registerFragment) }
        binding.loginButton.setOnClickListener {

            mRegisterLoginViewModel.login(email.text.toString(), password.text.toString())

             }


        mRegisterLoginViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                if (it == "Successfully logged in!") {
                    binding.email.text.clear()
                    binding.password.text.clear()
                    val intent= Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        })

    }
}