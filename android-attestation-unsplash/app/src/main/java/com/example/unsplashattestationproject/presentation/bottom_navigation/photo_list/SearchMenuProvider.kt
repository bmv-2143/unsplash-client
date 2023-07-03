package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import com.example.unsplashattestationproject.R

class SearchMenuProvider(
    private val context: Context,
    private val previousSearchQuery: String?,
    private val startSearchAction: (query : String) -> Unit,
    private val onQueryTextChangedAction: (query : String) -> Unit,
    private val clearSearchAction: () -> Unit
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
        if (!previousSearchQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(previousSearchQuery, false)
        }
    }

    private fun setOnQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Search SUBMITTED", Toast.LENGTH_SHORT).show()
                if (!query.isNullOrEmpty()) {
                    startSearchAction(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onQueryTextChangedAction(newText ?: "")
                return true
            }
        })
    }

    private fun setOnActionExpandListener(searchItem: MenuItem) {
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                clearSearchAction()
                return true
            }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_search -> {
                Toast.makeText(context, "Search clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> false
        }
    }
}