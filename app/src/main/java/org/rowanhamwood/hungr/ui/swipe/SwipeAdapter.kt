package org.rowanhamwood.hungr.ui.swipe


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.rowanhamwood.hungr.databinding.SwipeItemViewBinding
import org.rowanhamwood.hungr.network.Recipe
import org.rowanhamwood.hungr.network.RecipeModel


class SwipeAdapter :ListAdapter<Recipe,SwipeAdapter.SwipeViewHolder>(DiffCallback) {

    class SwipeViewHolder(private var binding: SwipeItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe: Recipe){
            binding.recipe = recipe
            binding.executePendingBindings()

        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        return SwipeViewHolder(SwipeItemViewBinding.inflate(
            LayoutInflater.from(parent.context)))
    }



    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.bind(recipe)

    }





    companion object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipeInfo.uri == newItem.recipeInfo.uri
        }
    }

}