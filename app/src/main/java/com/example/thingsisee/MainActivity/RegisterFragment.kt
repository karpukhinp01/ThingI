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
import com.example.thingsisee.Data.LoadStatus
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email= binding.email
        val password=binding.password

        mMainViewModel.status.observe(viewLifecycleOwner) { status ->
            if (status == LoadStatus.REGISTERED) {
                binding.email.text.clear()
                binding.password.text.clear()
                findNavController().navigate(R.id.action_registerFragment_to_userInfoFragment)
                mMainViewModel.resetStatus()
            }
        }

        binding.login.setOnClickListener { findNavController()
            .navigate(R.id.action_registerFragment_to_loginFragment) }

        binding.registerButton.setOnClickListener {
            if (mMainViewModel.checkInputs(email.text.toString(), password.text.toString())
                && mMainViewModel.checkPassword(password.text.toString()) == ""){
                mMainViewModel.register(email.text.toString(), password.text.toString())
            }
            else if (!mMainViewModel.checkInputs(email.text.toString(), password.text.toString())) {
                Toast.makeText(requireContext(), "Please fill out the fields!", Toast.LENGTH_LONG).show()
            } else Toast.makeText(requireContext(), mMainViewModel.checkPassword(password.text.toString()), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}