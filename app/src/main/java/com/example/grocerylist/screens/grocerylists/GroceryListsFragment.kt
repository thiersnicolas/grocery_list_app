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
import com.example.grocerylist.databinding.FragmentGroceryListsBinding
import com.example.grocerylist.screens.MainActivityDirections
import com.example.grocerylist.screens.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroceryListsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var groceryListsViewModel: GroceryListsViewModel
    private lateinit var userViewModel: UserViewModel

    private var _binding: FragmentGroceryListsBinding? = null

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
        _binding = FragmentGroceryListsBinding.inflate(inflater, container, false)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        groceryListsViewModel =
            ViewModelProvider(requireActivity())[GroceryListsViewModel::class.java]

        val adapter = GroceryListAdapter(
            GroceryListListener { groceryListId ->
                this.findNavController().navigate(
                    GroceryListsFragmentDirections.actionGroceryListsFragmentToGroceryListDetailFragment(
                        groceryListId
                    )
                )
            },
            userViewModel.activeUser
        )

        binding.groceryLists.adapter = adapter
        binding.searchGroceryListTextField.setOnKeyListener { _, _, _ ->
            adapter.filter(binding.searchGroceryListTextField.text.toString())
            false
        }

        groceryListsViewModel.groceryLists.observe(viewLifecycleOwner) { groceryLists ->
            adapter.modifyList(groceryLists)
        }
        groceryListsViewModel.createGroceryListClicked.observe(viewLifecycleOwner) {
            if (it == true) navController.navigate(R.id.action_groceryListsFragment_to_createGroceryListFragment)
        }

        binding.backAppBar.setNavigationOnClickListener {
            navController.navigate(MainActivityDirections.actionGlobalNavWelcome())
        }

        binding.viewModel = groceryListsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
