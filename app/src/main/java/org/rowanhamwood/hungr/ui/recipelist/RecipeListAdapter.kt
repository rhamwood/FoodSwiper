package org.rowanhamwood.hungr.ui.recipelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.rowanhamwood.hungr.databinding.RecipeListItemViewBinding
import org.rowanhamwood.hungr.network.RecipeModel


class RecipeListAdapter(val clickListener: RecipeListListener) : ListAdapter<RecipeModel, RecipeListAdapter.RecipeListViewHolder>(DiffCallback) {

    class RecipeListViewHolder(private var binding: RecipeListItemViewBinding ) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe: RecipeModel, clickListener: RecipeListListener){
            binding.recipe = recipe
            binding.clickListener = clickListener
            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        return RecipeListViewHolder(
            RecipeListItemViewBinding.inflate(
            LayoutInflater.from(parent.context)))
    }



    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe, clickListener)

    }


    companion object DiffCallback : DiffUtil.ItemCallback<RecipeModel>() {
        override fun areItemsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
            return oldItem.uri == newItem.uri
        }
    }

    class RecipeListListener(val clickListener: (recipeUrl: String) -> Unit) {
        fun onClick(recipe: RecipeModel) = clickListener(recipe.url)
    }



}