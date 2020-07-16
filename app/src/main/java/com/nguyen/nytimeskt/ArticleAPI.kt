package com.nguyen.nytimeskt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleAPI {
    // https://api.nytimes.com/svc/search/v2/articlesearch.json?q=messi&api-key=GYWXJ04BtYKmLWLwGouVEON0y34KNYgh
    @GET("articlesearch.json")
    fun fetchPage(@Query("q") query: String?, @Query("page") page: Int, @Query("begin_date") beginDate: String?, @Query("fq") filterQuery: String?, @Query("sort") sort: String?, @Query("api-key") apiKey: String) : Call<Json>
}
