package com.clickandsave.saawanshah.dispatchnews.Fragments.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.clickandsave.saawanshah.dispatchnews.Adapter.NewsAdapter
import com.clickandsave.saawanshah.dispatchnews.Data.NewsArticle
import com.clickandsave.saawanshah.dispatchnews.Data.NewsCategory
import com.clickandsave.saawanshah.dispatchnews.Fragments.home.adapters.BreakingNewsAdapter
import com.clickandsave.saawanshah.dispatchnews.Repository.NewsRepository
import com.clickandsave.saawanshah.dispatchnews.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val newsRepository       = NewsRepository()
    private val newsAdapter          = NewsAdapter()
    private val breakingNewsAdapter  = BreakingNewsAdapter()

    /** nextPage token for the currently selected category — used for pagination */
    private var currentNextPage: String? = null

    /** Keep a reference so we can cancel in-flight requests when the tab changes */
    private var fetchJob: Job? = null

    /** Cache key prefix — each category gets its own cache slot */
    private val PREFS_NAME = "dispatch_news_prefs"

    // All tabs (in order).  ALL maps to getLatestNews(), others → getNewsByCategory()
    private val categories = NewsCategory.values().toList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewPager()
        setupTabLayout()
        // Load the first tab (All) on startup
        fetchNewsForCategory(NewsCategory.ALL)
    }

    // -------------------------------------------------------------------------
    // Setup helpers
    // -------------------------------------------------------------------------

    private fun setupRecyclerView() {
        binding.newsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun setupViewPager() {
        binding.breakingNewsPager.apply {
            adapter = breakingNewsAdapter
        }
    }

    private fun setupTabLayout() {
        categories.forEach { category ->
            binding.CategoriesTabLayout.addTab(
                binding.CategoriesTabLayout.newTab().setText(category.displayName)
            )
        }

        binding.CategoriesTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val selectedCategory = categories.getOrNull(tab?.position ?: 0) ?: NewsCategory.ALL
                fetchNewsForCategory(selectedCategory)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    // -------------------------------------------------------------------------
    // Core fetch — calls the correct API endpoint based on selected category
    // -------------------------------------------------------------------------

    private fun fetchNewsForCategory(category: NewsCategory) {
        // Cancel any in-flight job first
        fetchJob?.cancel()

        binding.progressBar.visibility = View.VISIBLE

        // Show cached data immediately while we fetch fresh content
        val cached = getCachedNews(category)
        if (cached.isNotEmpty()) {
            displayNews(cached, isFirstLoad = true)
        }

        fetchJob = viewLifecycleOwner.lifecycleScope.launch {
            val result = if (category == NewsCategory.ALL) {
                newsRepository.getLatestNews()
            } else {
                newsRepository.getNewsByCategory(category.value)
            }

            binding.progressBar.visibility = View.GONE

            result.onSuccess { (articles, nextPage) ->
                currentNextPage = nextPage
                if (articles.isNotEmpty()) {
                    saveNewsToCache(category, articles)
                    displayNews(articles, isFirstLoad = true)
                }
            }.onFailure { exception ->
                Log.e("HomeFragment", "Failed to fetch ${category.displayName} news", exception)
                if (cached.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load news: ${exception.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Couldn't refresh. Showing cached ${category.displayName} news.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Pagination — load next page for the current category
    // -------------------------------------------------------------------------

    fun loadNextPage() {
        val token = currentNextPage ?: return   // nothing more to load

        viewLifecycleOwner.lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            val result = newsRepository.getNextPage(token)
            binding.progressBar.visibility = View.GONE

            result.onSuccess { (articles, nextPage) ->
                currentNextPage = nextPage
                displayNews(articles, isFirstLoad = false)
            }.onFailure { exception ->
                Toast.makeText(
                    requireContext(),
                    "Failed to load more: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // -------------------------------------------------------------------------
    // Display
    // -------------------------------------------------------------------------

    /**
     * @param isFirstLoad true  → replace the list (new category / fresh fetch)
     *                    false → append to existing list (pagination)
     */
    private fun displayNews(articles: List<NewsArticle>, isFirstLoad: Boolean) {
        if (isFirstLoad) {
            val breaking = articles.take(5)
            val regular  = articles.drop(5)
            breakingNewsAdapter.updateData(breaking)
            newsAdapter.updateData(regular)
        } else {
            // Pagination: append to current list
            newsAdapter.appendData(articles)
        }
    }

    // -------------------------------------------------------------------------
    // Cache helpers — keyed per category so each tab has its own cache slot
    // -------------------------------------------------------------------------

    private fun cacheKey(category: NewsCategory) = "cached_news_${category.name}"

    private fun getCachedNews(category: NewsCategory): List<NewsArticle> {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json  = prefs.getString(cacheKey(category), null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<NewsArticle>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveNewsToCache(category: NewsCategory, articles: List<NewsArticle>) {
        try {
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putString(cacheKey(category), Gson().toJson(articles)).apply()
        } catch (e: Exception) {
            Log.e("HomeFragment", "Failed to cache news for ${category.displayName}", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}