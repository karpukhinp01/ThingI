package com.example.thingsisee

import android.app.Activity
import android.content.Context
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
import com.example.thingsi.Data.Post
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private var _binding: FragmentFirstBinding? = null
    private lateinit var mMainViewModel: MainViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.statusMessage.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })

        val postNameET = binding.postNameValue
        val postTextET = binding.postTextValue

        binding.buttonGoToList.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_postFragment)
        }

        binding.postButton.setOnClickListener {
            mMainViewModel.insertData(postNameET.text.toString(), postTextET.text.toString())
            postNameET.text.clear()
            postTextET.text.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}