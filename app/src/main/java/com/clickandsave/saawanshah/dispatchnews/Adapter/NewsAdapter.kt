package com.clickandsave.saawanshah.dispatchnews.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clickandsave.saawanshah.dispatchnews.Data.NewsArticle
import com.clickandsave.saawanshah.dispatchnews.databinding.ItemNewsBinding

class NewsAdapter(private var articles: MutableList<NewsArticle> = mutableListOf()) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    /** Replace the entire list (used when switching categories or doing a fresh fetch) */
    fun updateData(newArticles: List<NewsArticle>) {
        articles = newArticles.toMutableList()
        notifyDataSetChanged()
    }

    /** Append articles to the existing list (used for pagination / load-more) */
    fun appendData(moreArticles: List<NewsArticle>) {
        val start = articles.size
        articles.addAll(moreArticles)
        notifyItemRangeInserted(start, moreArticles.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int = articles.size

    class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: NewsArticle) {
            binding.tvTitle.text       = article.title
            binding.tvDescription.text = article.description ?: ""
            binding.tvCategory.text    = article.category?.firstOrNull() ?: "News"

            Glide.with(binding.root.context)
                .load(article.image_url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.imgNews)
        }
    }
}