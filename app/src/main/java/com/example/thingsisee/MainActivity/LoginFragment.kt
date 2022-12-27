package com.example.thingsisee.MainActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            if (authState == AuthState.AUTHENTIFICATED) {
                binding.email.text.clear()
                binding.password.text.clear()
                findNavController().navigate(R.id.action_loginFragment_to_postFragment)
            }
        })
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
            if (mMainViewModel.checkInputs(email.text.toString(), password.text.toString())) {
                mMainViewModel.login(email.text.toString(), password.text.toString())
            } else Toast.makeText(requireContext(), "Please fill out the fields", Toast.LENGTH_LONG).show()
        }

    }
}