package com.example.thingsisee.MainActivity

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.Data.LoadStatus
import com.example.thingsisee.R
import com.example.thingsisee.R.*
import com.example.thingsisee.ViewModels.MainViewModel
import com.example.thingsisee.databinding.FragmentPostListBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
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
        var statusMessages: String = "ds"
        val dialog = BottomSheetDialog(requireContext())
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


        mMainViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                LoadStatus.SUCCESS -> {
                    dialog.dismiss()
                    statusMessages = ""
                    mMainViewModel.resetStatus()
                }

                LoadStatus.PENDING -> {
                    statusMessages = "Getting there..."
                }

                else -> statusMessages = "ds"
            }
        }


        binding.fab1.setOnClickListener {



            val view = layoutInflater.inflate(layout.card_post_dialogue, null)

            val profilePic = view.findViewById<ImageView>(R.id.profile_picture)

            profilePic.setBackgroundResource(R.drawable.user)

            dialog.setCancelable(true)

            dialog.setContentView(view)

            dialog.show()

            val postButton = view.findViewById<TextView>(R.id.post_button)
            val postText = view.findViewById<TextInputEditText>(R.id.post_text_value)
            val statusMessage = view.findViewById<TextView>(R.id.status_message)

            statusMessage.text = statusMessages


            postButton.setOnClickListener {
                mMainViewModel.insertData(FirebaseAuth.getInstance().currentUser!!.displayName.toString(), postText.text.toString())
                mMainViewModel.setStatus(LoadStatus.PENDING)
            }
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
