package org.rowanhamwood.hungr.ui.recipelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.rowanhamwood.hungr.databinding.RecipeListItemViewBinding
import org.rowanhamwood.hungr.local.database.DatabaseRecipe


class RecipeListAdapter(val clickListener: RecipeListListener) :
    ListAdapter<DatabaseRecipe, RecipeListAdapter.RecipeListViewHolder>(DiffCallback) {

    class RecipeListViewHolder(private var binding: RecipeListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: DatabaseRecipe, clickListener: RecipeListListener) {
            binding.recipe = recipe
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        return RecipeListViewHolder(
            RecipeListItemViewBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }


    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, clickListener)

    }


    companion object DiffCallback : DiffUtil.ItemCallback<DatabaseRecipe>() {
        override fun areItemsTheSame(oldItem: DatabaseRecipe, newItem: DatabaseRecipe): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DatabaseRecipe, newItem: DatabaseRecipe): Boolean {
            return oldItem.uri == newItem.uri
        }
    }

    class RecipeListListener(val clickListener: (recipeUrl: String) -> Unit) {
        fun onClick(recipe: DatabaseRecipe) = clickListener(recipe.url)
    }


}