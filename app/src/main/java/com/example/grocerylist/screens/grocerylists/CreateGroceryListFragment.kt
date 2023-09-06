package com.example.grocerylist.screens.grocerylists

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
import com.example.grocerylist.databinding.FragmentCreateGroceryListBinding
import com.example.grocerylist.screens.MainActivityDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateGroceryListFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: CreateGroceryListViewModel

    private var _binding: FragmentCreateGroceryListBinding? = null

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
        _binding = FragmentCreateGroceryListBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[CreateGroceryListViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.backAppBar.setNavigationOnClickListener {
            navController.navigate(MainActivityDirections.actionGlobalNavWelcome())
        }

        setCreateHandler()
        setNameValidationHandler()
        return view
    }

    override fun onStop() {
        viewModel.clearGroceryListDetail()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setCreateHandler() {
        binding.createButton.setOnClickListener {
            val groceryListName = binding.groceryListNameInputField.text.toString().trim()
            if (isNameValid(groceryListName)) {
                viewModel.createGroceryList(groceryListName)
                viewModel.groceryListDetail.observe(viewLifecycleOwner) { groceryListDetail ->
                    if (groceryListDetail != null) {
                        this.findNavController().navigate(
                            CreateGroceryListFragmentDirections.actionCreateGroceryListFragmentToGroceryListDetailFragment(
                                groceryListDetail.id
                            )
                        )
                    }
                }
            } else {
                binding.groceryListNameTextLayout.error =
                    getString(R.string.grocery_list_name_invalid)
            }
        }
    }

    private fun setNameValidationHandler() {
        binding.groceryListNameInputField.setOnKeyListener { _, _, _ ->
            if (isNameValid(binding.groceryListNameInputField.text.toString())) {
                binding.groceryListNameTextLayout.error = null
            }
            false
        }
    }

    private fun isNameValid(name: String): Boolean {
        return !(name.isBlank() || name.length <= 3)
    }
}
