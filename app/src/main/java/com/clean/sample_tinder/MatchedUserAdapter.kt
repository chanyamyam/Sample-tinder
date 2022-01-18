package com.clean.sample_tinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class MatchedUserAdapter: ListAdapter<CardItem,MatchedUserAdapter.ViewHolder>(diffutil) {

    inner class ViewHolder(private val view: View):RecyclerView.ViewHolder(view) {

        fun bind(cardItem: CardItem) {
            view.findViewById<TextView>(R.id.userNameTextView).text = cardItem.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_matched_user,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
    companion object {
        val diffutil = object : DiffUtil.ItemCallback<CardItem>() {
            override fun areItemsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: CardItem, newItem: CardItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}