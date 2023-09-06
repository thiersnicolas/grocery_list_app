package com.example.grocerylist.screens.grocerylists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.grocerylist.databinding.GroceryListItemBinding
import com.example.grocerylist.domain.AppUser
import com.example.grocerylist.domain.GroceryList
import com.google.gson.Gson
import timber.log.Timber
import java.util.*

class GroceryListAdapter(
    val clickListener: GroceryListListener
) :
    ListAdapter<GroceryList, ViewHolder>(GroceryListDiffCallback()) {
    private var unfilteredList: List<GroceryList> = emptyList()
    private var user: AppUser? = null

    fun modifyList(list: List<GroceryList>) {
        unfilteredList = list
        submitList(list)
    }

    fun addUser(user: AppUser?) {
        this.user = user
    }

    fun filter(filter: CharSequence?) {
        if (!filter.isNullOrEmpty()) {
            submitList(unfilteredList.filter { it.name.contains(filter, true) })
        } else {
            submitList(unfilteredList)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, user)
    }
}


class ViewHolder(val binding: GroceryListItemBinding, val user: AppUser?) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(clickListener: GroceryListListener, item: GroceryList) {
        binding.groceryListNameTextview.text = item.name
        binding.groceryListStatusTextview.text = item.status

        Timber.tag("GroceryListAdapter")
            .i("binding groceryList: %s; and user: %s", gson.toJson(item), gson.toJson(user))

        binding.groceryList = item
        binding.clickListener = clickListener
        binding.user = user
        binding.executePendingBindings()


    }

    companion object {
        fun from(parent: ViewGroup, user: AppUser?): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = GroceryListItemBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding, user)
        }

        private val gson: Gson = Gson()
    }
}

class GroceryListDiffCallback : DiffUtil.ItemCallback<GroceryList>() {
    override fun areItemsTheSame(oldItem: GroceryList, newItem: GroceryList): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GroceryList, newItem: GroceryList): Boolean {
        return oldItem == newItem
    }
}

class GroceryListListener(val clickListener: (groceryListIf: UUID) -> Unit) {
    fun onClick(groceryList: GroceryList) = clickListener(groceryList.id)
}