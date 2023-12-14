package com.example.examexampleapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.examexampleapp.R
import com.example.examexampleapp.databinding.ItemOfNewsBinding
import com.example.examexampleapp.models.News

class NewsAdapter(con: Context) : ListAdapter<News, NewsAdapter.Holder>(Comparator()) {

    class Holder(view: View, con: Context) : RecyclerView.ViewHolder(view){
        private val binding = ItemOfNewsBinding.bind(view)

        private val con = con

        fun bind(news: News) = with(binding){
            newsName.text = news.name
            newsDesc.text = news.description
            newsPrice.text = news.price.toString() + " ₽"

            Glide.with(con).load(news.image).into(photoFromNews)

        }

    }

    class Comparator: DiffUtil.ItemCallback<News>(){
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_of_news,
            parent, false)
        return Holder(view, parent.context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

}