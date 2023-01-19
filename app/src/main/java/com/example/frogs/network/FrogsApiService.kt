package com.example.frogs.network

import com.example.frogs.model.FrogRec
import retrofit2.http.GET

/**
 * A public interface that exposes the [getFrogsRecords] method
 */
interface FrogsApiService {

    companion object {
        const val BASE_URL = "https://android-kotlin-fun-mars-server.appspot.com/"
    }

    /**
     * Returns a [List] of [FrogRec] and this method can be called from a Coroutine.
     * The @GET annotation indicates that the "photos" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("amphibians")
    suspend fun getFrogsRecords(): List<FrogRec>
}