package com.example.grocerylist.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.R
import com.example.grocerylist.databinding.FragmentUnknownUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UnknownUserFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    private var _binding: FragmentUnknownUserBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUnknownUserBinding.inflate(inflater, container, false)
        val view = binding.root
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.toLoginButton.setOnClickListener {
            navController.navigate(UnknownUserFragmentDirections.actionUnknownUserFragmentToLoginFragment())
        }

        binding.toRegisterButton.setOnClickListener {
            navController.navigate(UnknownUserFragmentDirections.actionUnknownUserFragmentToRegisterFragment())
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
