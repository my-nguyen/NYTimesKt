package com.nguyen.nytimeskt

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {
    companion object {
        val NYTIMES_BASE_URL = "https://api.nytimes.com/svc/search/v2/"
    }

    @Provides
    fun provideGsonConverterFactory() : GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(factory: GsonConverterFactory) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(NYTIMES_BASE_URL)
            .addConverterFactory(factory)
            .build()
    }

    @Provides
    fun provideArticleAPI(retrofit: Retrofit) : ArticleAPI {
        return retrofit.create(ArticleAPI::class.java)
    }
}