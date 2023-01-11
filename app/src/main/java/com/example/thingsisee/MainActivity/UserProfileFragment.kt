package com.example.thingsisee.MainActivity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class UserProfileFragment : Fragment() {
    private lateinit var mMainViewModel: MainViewModel
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!


    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d("pic3", "${result.resultCode}")
            Log.d("pic4", "${result.data}")
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                mMainViewModel.selectedUri = result.data!!.data
                binding.userPic.setImageResource(R.drawable.user)
                mMainViewModel.uploadUserPicToFirebaseStorage()

            }
        }


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
        mMainViewModel.updateUri()
        binding.profileToolbar.inflateMenu(R.menu.menu_main)
        binding.userName.text = mMainViewModel.user!!.displayName
        binding.email.text = mMainViewModel.user!!.email
        var profilePic = binding.userPic


        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_postFragment)
        }
        // getting the recyclerview by its id
        val recyclerView = binding.recyclerView
        val adapter = PostRecyclerViewAdapter(2, requireContext())
        // Setting the Adapter with the recyclerview
        recyclerView.adapter = adapter
        // this creates a vertical layout Manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val userPosts =
            mMainViewModel.getUserPosts(FirebaseAuth.getInstance().currentUser?.displayName.toString())

        binding.selectProfilePic.setOnClickListener {
            mMainViewModel.uploadUserPic(startForResult)
        }

        mMainViewModel.profilePicUrl.observe(viewLifecycleOwner) {
            Picasso.with(requireContext()).load(mMainViewModel.profilePicUrl.value).into(profilePic)
        }

        userPosts.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }

    }
}
