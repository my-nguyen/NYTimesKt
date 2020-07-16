package com.nguyen.nytimeskt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(val articleAPI: ArticleAPI) {
    // val NYTIMES_BASE_URL = "https://api.nytimes.com/svc/search/v2/"
    val TAG = "ArticleRepository"

    /*init {
        val retrofit = Retrofit.Builder()
            .baseUrl(NYTIMES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        articleAPI = retrofit.create(ArticleAPI::class.java)
    }*/

    fun fetchPage(query: String?, page: Int, beginDate: String?, filterQuery: String?, sort: String?, apiKey: String): LiveData<List<Article>> {
        Log.d(TAG, "fetchPage: query: " + query + ", page: " + page + ", beginDate: " + beginDate + ", filterQuery: " + filterQuery + ", sort: " + sort
        )
        val data = MutableLiveData<List<Article>>()
        articleAPI.fetchPage(query, page, beginDate, filterQuery, sort, apiKey)
            .enqueue(object : Callback<Json> {
                override fun onResponse(call: Call<Json>, response: retrofit2.Response<Json>) {
                    val articles = mutableListOf<Article>()
                    response.body()?.let {
                        for (doc in it.response.docs) {
                            val article = Article(doc)
                            articles.add(article)
                        }
                    }

                    Log.d(TAG, "articles: " + articles.size)
                    data.setValue(articles)
                }

                override fun onFailure(call: Call<Json>, t: Throwable) {
                    Log.d(TAG, "onFailure")
                }
            })
        return data
    }
}
