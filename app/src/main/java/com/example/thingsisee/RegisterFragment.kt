package com.example.thingsisee

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.ViewModels.RegisterLoginViewModel
import com.example.thingsisee.databinding.FragmentRegisterBinding
import kotlinx.android.synthetic.main.fragment_register.*

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
            mRegisterLoginViewModel.register(email.text.toString(), password.text.toString())

            }

        mRegisterLoginViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                if (it == "Successfully registered!") {
                    val intent= Intent(activity, MainActivity::class.java)
                    binding.email.text.clear()
                    binding.password.text.clear()
                    startActivity(intent)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}