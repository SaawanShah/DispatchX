package com.clickandsave.saawanshah.dispatchnews.Fragments.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.clickandsave.saawanshah.dispatchnews.Data.NewsArticle
import com.clickandsave.saawanshah.dispatchnews.databinding.ItemBreakingNewsBinding

class BreakingNewsAdapter(
    private var newsList: List<NewsArticle> = emptyList()
) : RecyclerView.Adapter<BreakingNewsAdapter.BreakingNewsViewHolder>() {

    inner class BreakingNewsViewHolder(
        private val binding: ItemBreakingNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(news: NewsArticle) {
            binding.tvTitle.text = news.title
            binding.tvCategory.text = news.category?.firstOrNull() ?: "Breaking"

            Glide.with(binding.root.context)
                .load(news.image_url)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(binding.ivBreakingNews)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakingNewsViewHolder {
        val binding = ItemBreakingNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BreakingNewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreakingNewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount() = newsList.size

    fun updateData(list: List<NewsArticle>) {
        newsList = list
        notifyDataSetChanged()
    }
}
