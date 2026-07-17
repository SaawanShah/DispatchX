package com.clickandsave.saawanshah.dispatchnews.Fragments.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.clickandsave.saawanshah.dispatchnews.Adapter.NewsAdapter
import com.clickandsave.saawanshah.dispatchnews.Repository.NewsRepository
import com.clickandsave.saawanshah.dispatchnews.databinding.FragmentSearchBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val newsRepository = NewsRepository()
    private val searchAdapter  = NewsAdapter()

    /** Holds the debounce coroutine so we can cancel the previous one on each keystroke */
    private var debounceJob: Job? = null

    /** nextPage token for the current search query */
    private var currentNextPage: String? = null
    private var currentQuery: String     = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchBar()
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private fun setupRecyclerView() {
        binding.searchResultsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchAdapter
        }
    }

    private fun setupSearchBar() {
        // Debounce: wait 500ms after the user stops typing before hitting the API
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""

                if (query.isEmpty()) {
                    // User cleared the search bar — go back to empty / prompt state
                    showEmptyState(prompt = true)
                    return
                }

                // Cancel any pending debounce job
                debounceJob?.cancel()
                debounceJob = viewLifecycleOwner.lifecycleScope.launch {
                    delay(500L)          // debounce window
                    performSearch(query)
                }
            }
        })

        // Also respond to the keyboard's Search / Done action
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.etSearch.text?.toString()?.trim() ?: ""
                if (query.isNotEmpty()) {
                    debounceJob?.cancel()
                    performSearch(query)
                }
                true
            } else {
                false
            }
        }
    }

    // -------------------------------------------------------------------------
    // Search
    // -------------------------------------------------------------------------

    private fun performSearch(query: String) {
        currentQuery    = query
        currentNextPage = null

        showLoading(true)
        hideEmptyState()

        viewLifecycleOwner.lifecycleScope.launch {
            val result = newsRepository.searchNews(query)

            showLoading(false)

            result.onSuccess { (articles, nextPage) ->
                currentNextPage = nextPage
                if (articles.isEmpty()) {
                    showEmptyState(prompt = false, query = query)
                } else {
                    searchAdapter.updateData(articles)
                    binding.searchResultsRecycler.visibility = View.VISIBLE
                }
            }.onFailure { exception ->
                showEmptyState(prompt = false, query = query)
                Toast.makeText(
                    requireContext(),
                    "Search failed: ${exception.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /** Load the next page for the current query (call this from a "Load More" button if needed) */
    fun loadNextPage() {
        val token = currentNextPage ?: return
        val query = currentQuery.ifEmpty { return }

        viewLifecycleOwner.lifecycleScope.launch {
            showLoading(true)
            val result = newsRepository.getNextPage(token)
            showLoading(false)

            result.onSuccess { (articles, nextPage) ->
                currentNextPage = nextPage
                searchAdapter.appendData(articles)
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
    // UI state helpers
    // -------------------------------------------------------------------------

    private fun showLoading(show: Boolean) {
        binding.searchProgressBar.visibility = if (show) View.VISIBLE else View.GONE
        if (show) binding.searchResultsRecycler.visibility = View.GONE
    }

    private fun showEmptyState(prompt: Boolean, query: String = "") {
        binding.emptyState.visibility            = View.VISIBLE
        binding.searchResultsRecycler.visibility = View.GONE

        if (prompt) {
            // Default "Find what you need" prompt
            binding.tvEmptyTitle.text    = "Find what you need"
            binding.tvEmptySubtitle.text = "Explore articles, topics, and publishers from all over the world."
        } else {
            // No results for this query
            binding.tvEmptyTitle.text    = "No results found"
            binding.tvEmptySubtitle.text = "We couldn't find any news for \"$query\". Try a different keyword."
        }
    }

    private fun hideEmptyState() {
        binding.emptyState.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
