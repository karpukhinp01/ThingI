package com.example.thingsisee.MainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.R
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentPostListBinding

/**
 * A fragment representing a list of Items.
 */
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

    }
}