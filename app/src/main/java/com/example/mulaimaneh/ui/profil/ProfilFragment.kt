package com.example.mulaimaneh.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mulaimaneh.LoginActivity
import com.example.mulaimaneh.UserPreferences
import com.example.mulaimaneh.databinding.FragmentProfilBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfilFragment : Fragment() {

    private var _binding: FragmentProfilBinding? = null
    private val binding get() = _binding!!
    private val userPreferences by lazy { UserPreferences(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainScope().launch {
            val githubUsername = userPreferences.getGithubUsername()
            val userEmail = userPreferences.getEmail()

            binding.nama.text = githubUsername
            binding.emil.text = userEmail
            loadGithubAvatar(githubUsername, binding.image)
        }
        binding.imageView2.setOnClickListener {
            logout()
        }
    }

    private fun loadGithubAvatar(username: String, imageView: ImageView) {
        val avatarUrl = "https://github.com/$username.png"
        Glide.with(this)
            .load(avatarUrl)
            .circleCrop()
            .into(imageView)
    }

    private fun logout() {
        MainScope().launch {
            userPreferences.clearUserPreferences()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
