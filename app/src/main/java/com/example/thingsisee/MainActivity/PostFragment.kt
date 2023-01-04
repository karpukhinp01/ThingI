package com.example.thingsisee.MainActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.R
import com.example.thingsisee.R.*
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentPostListBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth


class PostFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel
    private var _binding: FragmentPostListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getting the recyclerview by its id
        val recyclerview = binding.recyclerview
        val adapter = PostRecyclerViewAdapter()
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.allPosts.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_FirstFragment2)
        }

        binding.postsToolbar.inflateMenu(menu.menu_main)

        binding.profilePic.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_userProfileFragment)
        }

        mMainViewModel.authState.observe(viewLifecycleOwner) { authState ->
            if (authState == AuthState.UNAUTHENTIFICATED) {
                findNavController().navigate(R.id.action_postFragment_to_loginFragment)
            }
        }


        binding.fab1.setOnClickListener {

            val dialog = BottomSheetDialog(requireContext())

            val view = layoutInflater.inflate(layout.card_post_dialogue, null)

            val profilePic = view.findViewById<ImageView>(R.id.profile_picture)

            profilePic.setBackgroundResource(R.drawable.user)

            dialog.setCancelable(true)

            dialog.setContentView(view)

            dialog.show()
        }

        binding.postsToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_logout) {
                AuthUI.getInstance().signOut(requireContext())
                Log.d("Reg", FirebaseAuth.getInstance().currentUser.toString())
                true
            } else false
        }

    }
}