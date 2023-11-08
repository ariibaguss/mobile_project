package com.example.mulaimaneh.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mulaimaneh.R
import com.example.mulaimaneh.UserAdapter
import com.example.mulaimaneh.dao.SettingPreferences
import com.example.mulaimaneh.ui.detail.DetailActivity
import com.example.mulaimaneh.ui.favorite.FavoriteFragment
import com.example.mulaimaneh.model.ResponseUser
import com.example.mulaimaneh.setting.SettingActivity
import com.example.mulaimaneh.databinding.FragmentHomeBinding
import com.example.mulaimaneh.utils.Result

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val adapter by lazy {
        UserAdapter { user ->
            Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModel.Factory(SettingPreferences(requireContext()))
    }

    private var lastQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel.getTheme().observe(viewLifecycleOwner) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.setHasFixedSize(true)
        binding.rvUsers.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    if (newText != lastQuery) {
                        lastQuery = newText
                        viewModel.getUser("in:header $newText")
                    }
                } else {
                    lastQuery = ""
                    viewModel.getUser()
                }
                return true
            }
        })


        viewModel.resultUser.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success<*> -> {
                    adapter.setData(it.data as MutableList<ResponseUser.Item>)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getUser()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                Intent(requireContext(), FavoriteFragment::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.setting -> {
                Intent(requireContext(), SettingActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
