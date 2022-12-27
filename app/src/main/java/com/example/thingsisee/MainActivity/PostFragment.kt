package com.example.thingsisee.MainActivity

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentPostListBinding
import com.firebase.ui.auth.AuthUI
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
    ): View? {
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
        mMainViewModel.allPosts.observe(viewLifecycleOwner, Observer {
            adapter.updatePosts(it)
        })

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_postFragment_to_FirstFragment2)
        }

        binding.postsToolbar.inflateMenu(R.menu.menu_main)


        mMainViewModel.authState.observe(viewLifecycleOwner, Observer { authState ->
            if (authState == AuthState.UNAUTHENTIFICATED) {
                findNavController().navigate(R.id.action_postFragment_to_loginFragment)
            }
        })

        binding.postsToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.action_logout) {
                AuthUI.getInstance().signOut(requireContext())
                Log.d("Reg", FirebaseAuth.getInstance().currentUser.toString())
                true
            } else false
        }

    }
}