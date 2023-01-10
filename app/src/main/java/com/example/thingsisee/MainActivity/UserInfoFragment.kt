package com.example.thingsisee.MainActivity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.thingsisee.Data.LoadStatus
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentRegisterBinding
import com.example.thingsisee.databinding.FragmentUserInfoBinding
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserInfoBinding.inflate(inflater, container, false)
        mMainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mMainViewModel.status.observe(viewLifecycleOwner, Observer { status ->
            if (status == LoadStatus.SUCCESS) {
                findNavController().navigate(R.id.action_userInfoFragment_to_postFragment)
                mMainViewModel.resetStatus()
            }
        })

        val storageRef = FirebaseStorage.getInstance().getReference("/profilePics/Screenshot 2021-05-05 164534.png")
        storageRef.downloadUrl.addOnSuccessListener {
            val profilePicUpdate = userProfileChangeRequest {
                photoUri = it
            }
            mMainViewModel.user!!.updateProfile(profilePicUpdate).addOnSuccessListener {
                mMainViewModel.updateUri()}
        }


        binding.doneButton.setOnClickListener {
            val name = binding.enterName
            mMainViewModel.addUserInfo(name.text.toString())
        }
    }

}