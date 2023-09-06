package com.example.grocerylist.screens.grocerylists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.databinding.FragmentGroceryListDetailBinding
import com.example.grocerylist.screens.MainActivityDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroceryListDetailFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: GroceryListDetailViewModel

    private var _binding: FragmentGroceryListDetailBinding? = null

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
        _binding = FragmentGroceryListDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        viewModel = ViewModelProvider(requireActivity())[GroceryListDetailViewModel::class.java]

        viewModel.getGroceryListDetail(GroceryListDetailFragmentArgs.fromBundle(requireArguments()).groceryListId)

        binding.backAppBar.setNavigationOnClickListener {
            navController.navigate(MainActivityDirections.actionGlobalNavWelcome())
        }

        viewModel.navigateToGroceryLists.observe(viewLifecycleOwner) { groceryListId ->
            groceryListId?.let {
                viewModel.deleteGroceryListDetail(it)
                navController.navigate(GroceryListDetailFragmentDirections.actionGroceryListDetailFragmentToGroceryListsFragment())
                viewModel.doneNavigatingToGroceryLists()
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
