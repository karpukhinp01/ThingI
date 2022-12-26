package com.example.thingsisee.MainActivity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.RegisterLoginViewModel
import com.example.thingsisee.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mRegisterLoginViewModel: RegisterLoginViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        mRegisterLoginViewModel = ViewModelProvider(this).get(RegisterLoginViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email= binding.email
        val password=binding.password

        binding.login.setOnClickListener { findNavController()
            .navigate(R.id.action_registerFragment_to_loginFragment) }

        binding.registerButton.setOnClickListener {
            if (mRegisterLoginViewModel.checkInputs(email.text.toString(), password.text.toString())
                && mRegisterLoginViewModel.checkPassword(password.text.toString()) == ""){
                mRegisterLoginViewModel.register(email.text.toString(), password.text.toString())
            }
            else if (!mRegisterLoginViewModel.checkInputs(email.text.toString(), password.text.toString())) {
                Toast.makeText(requireContext(), "Please fill out the fields!", Toast.LENGTH_LONG).show()
            } else Toast.makeText(requireContext(), mRegisterLoginViewModel.checkPassword(password.text.toString()), Toast.LENGTH_LONG).show()
        }

        mRegisterLoginViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                if (it == "Successfully registered!") {
                    binding.email.text.clear()
                    binding.password.text.clear()
                    findNavController().navigate(R.id.action_registerFragment_to_postFragment)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}