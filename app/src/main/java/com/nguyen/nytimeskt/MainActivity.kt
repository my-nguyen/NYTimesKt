package com.nguyen.nytimeskt

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.nguyen.nytimeskt.databinding.ActivityMainBinding
import javax.inject.Inject

/*
 * completed on 7/15/2020
 * converted to Kotlin the NYTimesSearch project
 */
class MainActivity : AppCompatActivity(), SettingsFragment.DialogListener {
    companion object {
        const val TAG = "MainActivity"
        const val NYTIMES_API_KEY = "GYWXJ04BtYKmLWLwGouVEON0y34KNYgh"
    }

    @Inject
    lateinit var mainViewModel: MainViewModel
    @Inject
    lateinit var settings: Settings

    private lateinit var binding: ActivityMainBinding

    private val articles = mutableListOf<Article>()
    private val adapter = ArticleAdapter(articles, this)
    var page = 0
    var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        // mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.recyclerView.adapter = adapter
        binding.queryBox.requestFocus()

        binding.searchButton.setOnClickListener {
            query = binding.queryBox.text.toString()
            if (!TextUtils.isEmpty(query)) {
                page = 0
                fetchPage()
                hideKeyboard()
            }
        }

        val layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.layoutManager = layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // perform query here
                this@MainActivity.query = query
                page = 0
                fetchPage()
                // workaround to avoid issues with some emulators and keyboard devices firing twice if
                // a keyboard enter is used. see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val dialog = SettingsFragment.newInstance(settings)
                dialog.show(supportFragmentManager, "SettingsDialog")
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun fetchPage() {
        mainViewModel.fetchPage(query, page, settings.getBeginDate(), settings.getFilterQuery(), settings.getOrder(), NYTIMES_API_KEY)
            .observe(this, Observer {
                if (it.isEmpty() || page == 0) {
                    val size = articles.size
                    // reset the adapter if this is a new query and the current dataset is not empty
                    if (size != 0) {
                        articles.clear()
                        adapter.notifyItemRangeRemoved(0, size)
                    }
                }
                if (it.isNotEmpty()) {
                    val size = articles.size
                    articles.addAll(it)
                    adapter.notifyItemRangeInserted(size, it.size)
                    // keep fetching on if current batch contains 10 articles
                    if (it.size == 10) {
                        page++
                        Log.d(TAG, "fetchPage, page: $page")
                        if (page % 3 != 0) {
                            // each NYTimes API call returns 10 articles, and the device screen fits
                            // 3 times as many articles
                            fetchPage()
                        }
                    }
                }
            })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    // receive data returned from SettingsFragment
    override fun onFinish(settings: Settings) {
        this.settings = settings
    }
}
