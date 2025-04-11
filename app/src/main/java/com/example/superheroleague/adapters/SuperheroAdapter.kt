package com.example.superheroleague.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.superheroleague.R
import com.example.superheroleague.data.Superhero
import com.example.superheroleague.databinding.ItemSuperheroBinding
import com.squareup.picasso.Picasso

class SuperheroAdapter(
    var items: List<Superhero>,
    val onItemClick: (position: Int) -> Unit
): Adapter<SuperheroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroViewHolder {
        //val view = LayoutInflater.from(parent.context).inflate(R.layout.item_superhero, parent, false)
        val binding = ItemSuperheroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SuperheroViewHolder, position: Int) {
        val superhero = items[position]
        holder.render(superhero)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun updateItems(items: List<Superhero>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class SuperheroViewHolder(val binding: ItemSuperheroBinding) : ViewHolder(binding.root) {

    fun render(superhero: Superhero) {
        binding.nameTextView.text = superhero.name
        Picasso.get().load(superhero.image.url).into(binding.avatarImageView)
    }
}