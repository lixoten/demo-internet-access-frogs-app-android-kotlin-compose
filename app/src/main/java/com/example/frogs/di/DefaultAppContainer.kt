package com.example.frogs.di

import com.example.frogs.data.DefaultFrogsRepository
import com.example.frogs.data.FrogsRepository
import com.example.frogs.network.FrogsApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create

class DefaultAppContainer : AppContainer {
    //json OptIn?
    @OptIn(ExperimentalSerializationApi::class)
    override val frogsApiService: FrogsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(FrogsApiService.BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    override val frogsRepository: FrogsRepository by lazy {
        DefaultFrogsRepository(frogsApiService)
    }
}