package com.example.grocerylist.screens.grocerylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.R
import com.example.grocerylist.databinding.FragmentCreateGroceryListBinding
import com.example.grocerylist.screens.MainActivityDirections
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

        binding.createButton.setOnClickListener {
            viewModel.createGroceryList(binding.groceryListNameInputField.text.toString())
        }

        viewModel.navigateToGroceryListDetail.observe(viewLifecycleOwner) { groceryListId ->
            groceryListId?.let {
                navController.navigate(
                    CreateGroceryListFragmentDirections.actionCreateGroceryListFragmentToGroceryListDetailFragment(
                        it
                    )
                )
                viewModel.doneNavigatingToGroceryListDetails()
            }
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
