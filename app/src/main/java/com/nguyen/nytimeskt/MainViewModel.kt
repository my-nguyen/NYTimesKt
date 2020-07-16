package com.nguyen.nytimeskt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(val repository: ArticleRepository) : ViewModel() {
    companion object {
        val TAG = "MainViewModel"
    }

    fun fetchPage(query: String?, page: Int, beginDate: String?, filterQuery: String?, sort: String?, apiKey: String) : LiveData<List<Article>> {
        return repository.fetchPage(query, page, beginDate, filterQuery, sort, apiKey)
    }
}
