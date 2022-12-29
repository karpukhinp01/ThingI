package com.example.thingsisee.MainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentUserProfileBinding

class UserProfileFragment : Fragment() {
    private lateinit var mMainViewModel: MainViewModel
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileToolbar.inflateMenu(R.menu.menu_main)
        binding.userName.text = mMainViewModel.user!!.displayName
        binding.email.text = mMainViewModel.user!!.email
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_postFragment)
        }
    }
}