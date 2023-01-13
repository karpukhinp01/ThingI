package com.example.thingsisee.MainActivity

import android.app.ActionBar.LayoutParams
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentEditBinding
import com.squareup.picasso.Picasso


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
        mMainViewModel.loadComments(args.post.postId!!)
        val params = binding.commentsHeading.layoutParams as ConstraintLayout.LayoutParams
        binding.postName.text = args.post.postName
        binding.postText.text = args.post.postText
        Picasso.with(requireContext()).load(args.userPic).into(binding.userPic)

        val recyclerview = binding.recyclerView
        val adapter = PostRecyclerViewAdapter(3, requireContext())
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        mMainViewModel.allComments.observe(viewLifecycleOwner) {
            adapter.updatePosts(it)
        }
        mMainViewModel.allUserPics.observe(viewLifecycleOwner) {
            adapter.updatePics(it)
        }

        if (args.post.uid == mMainViewModel.user!!.uid) {
            binding.editButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE
        }
        binding.deleteButton.setOnClickListener {
            mMainViewModel.deletePost(args.post)
            findNavController().navigateUp()
        }

        binding.editButton.setOnClickListener {
            binding.postText.visibility = View.GONE
            binding.editButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.editPost.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
        }

        binding.editButton.setOnClickListener {
            binding.postText.visibility = View.GONE
            binding.editButton.visibility = View.GONE
            binding.deleteButton.visibility = View.GONE
            binding.editPost.visibility = View.VISIBLE
            binding.editPost.setText(args.post.postText)
            binding.postEditButton.visibility = View.VISIBLE
            params.topToBottom = binding.editPost.id
            binding.commentsHeading.requestLayout()
        }

        binding.postEditButton.setOnClickListener {
            mMainViewModel.editPost(args.post.postId!!, args.post.timestamp!!,
                args.post.uid!!, args.post.postName!!, binding.editPost.text.toString())
            binding.postText.visibility = View.VISIBLE
            binding.editButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE
            binding.editPost.visibility = View.GONE
            binding.postEditButton.visibility = View.GONE
            params.topToBottom = binding.postText.id
            binding.commentsHeading.requestLayout()
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.temp.setOnClickListener {
            mMainViewModel.comment(args.post.postId!!, "Hello again!")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}