package org.rowanhamwood.hungr.ui.swipe


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.rowanhamwood.hungr.databinding.SwipeItemViewBinding
import org.rowanhamwood.hungr.remote.network.RecipeModel


class SwipeAdapter :ListAdapter<RecipeModel,SwipeAdapter.SwipeViewHolder>(DiffCallback) {

    class SwipeViewHolder(private var binding: SwipeItemViewBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(recipe: RecipeModel){
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





    companion object DiffCallback : DiffUtil.ItemCallback<RecipeModel>() {
        override fun areItemsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RecipeModel, newItem: RecipeModel): Boolean {
            return oldItem.uri == newItem.uri
        }
    }

}