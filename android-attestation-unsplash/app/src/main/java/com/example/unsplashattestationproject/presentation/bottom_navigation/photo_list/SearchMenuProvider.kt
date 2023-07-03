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
    private val startSearchAction: (query : String) -> Unit,
    private val clearSearchAction: () -> Unit
) : MenuProvider {

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.photo_list_fragment_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Search SUBMITTED", Toast.LENGTH_SHORT).show()

                if (!query.isNullOrEmpty()) {

                    startSearchAction(query)

//                    photoListViewModel.startSearch(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search query text change
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Handle search view expand event
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                // Handle search view collapse event
//                photoListViewModel.clearSearchResults()

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