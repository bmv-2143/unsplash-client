package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import com.example.unsplashattestationproject.R

class SearchMenuProvider(
    private val previousSearchQuery: String?,
    private val onSearchQuerySubmit: (query : String) -> Unit,
    private val onQueryTextChanged: (query : String) -> Unit,
    private val onMenuSearchExpanded: () -> Unit,
    private val onMenuSearchCollapsed: () -> Unit,
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.photo_list_fragment_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        setOnQueryTextListener(searchView)
        setOnActionExpandListener(searchItem)

        restorePreviousSearch(searchItem, searchView)
    }

    private fun restorePreviousSearch(
        searchItem: MenuItem,
        searchView: SearchView
    ) {
        previousSearchQuery?.let {
            searchItem.expandActionView()
            searchView.setQuery(previousSearchQuery, false)
        }
    }

    private fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    onSearchQuerySubmit(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    onQueryTextChanged(newText)
                }
                return true
            }
        })
    }

    private fun setOnActionExpandListener(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                onMenuSearchExpanded()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                onMenuSearchCollapsed()
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_search -> {
                true
            }

            else -> false
        }
    }
}