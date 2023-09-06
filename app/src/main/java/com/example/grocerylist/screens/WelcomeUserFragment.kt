package com.example.grocerylist.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.R
import com.example.grocerylist.databinding.FragmentRegisterBinding
import com.example.grocerylist.databinding.FragmentWelcomeUserBinding
import com.example.grocerylist.domain.CredentialsValidator.Companion.validatePassword
import com.example.grocerylist.domain.CredentialsValidator.Companion.validateEmail
import com.example.grocerylist.domain.CredentialsValidator.Companion.validateName
import com.example.grocerylist.model.network.rest.resources.RegisterRequest
import com.example.grocerylist.screens.grocerylists.CreateGroceryListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeUserFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel

    private var _binding: FragmentWelcomeUserBinding? = null

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
        _binding = FragmentWelcomeUserBinding.inflate(inflater, container, false)
        val view = binding.root
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        if(userViewModel.activeUser.value == null) {
            navController.navigate(R.id.action_welcomeUserFragment_to_unknownUserFragment)
        }

        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
