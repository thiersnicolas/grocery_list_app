package com.example.grocerylist.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.R
import com.example.grocerylist.databinding.FragmentLoginBinding
import com.example.grocerylist.domain.CredentialsValidator.Companion.validateEmail
import com.example.grocerylist.domain.CredentialsValidator.Companion.validatePassword
import com.example.grocerylist.model.network.rest.resources.LoginRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var userViewModel: UserViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = findNavController()
        savedStateHandle = navController.previousBackStackEntry!!.savedStateHandle
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        binding.viewModel = userViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setUpObservers()
        setLoginHandler()
        setCredentialsValidationHandler()
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObservers() {
        userViewModel.activeUser.observe(viewLifecycleOwner) { foundUser ->
            if (foundUser != null) {
                binding.emailInputField.error = null
                binding.passwordTextLayout.error = null
                navController.navigate(MainActivityDirections.actionGlobalNavWelcome())
            }
        }
    }

    private fun setLoginHandler() {
        binding.loginButton.setOnClickListener {
            if (!validateEmail(binding.emailInputField.text.toString())) {
                binding.emailTextLayout.error = getString(R.string.app_email_invalid)
            } else if (!validatePassword(binding.passwordInputField.text.toString())) {
                binding.passwordTextLayout.error = getString(R.string.app_password_invalid)
            } else {
                attemptLogin(
                    binding.emailInputField.text.toString(),
                    binding.passwordInputField.text.toString()
                )
            }
        }
    }

    private fun attemptLogin(username: String, password: String) {
        userViewModel.login(
            LoginRequest(username, password),
            binding.loginScrollView
        )
    }

    private fun setCredentialsValidationHandler() {
        binding.emailInputField.setOnKeyListener { _, _, _ ->
            if (validateEmail(binding.emailInputField.text.toString())) {
                binding.emailTextLayout.error = null
            }
            false
        }
        binding.passwordInputField.setOnKeyListener { _, _, _ ->
            if (validatePassword(binding.passwordInputField.text.toString())) {
                binding.passwordTextLayout.error = null
            }
            false
        }
    }
}
