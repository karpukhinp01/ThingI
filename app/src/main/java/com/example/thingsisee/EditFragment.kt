package com.example.thingsisee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentEditBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    private val args by navArgs<EditFragmentArgs>()
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.deleteButton.setOnClickListener {
            mMainViewModel.deletePost(args.post)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}