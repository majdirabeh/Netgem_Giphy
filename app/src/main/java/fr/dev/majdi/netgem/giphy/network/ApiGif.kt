package fr.dev.majdi.netgem.giphy.network

import fr.dev.majdi.netgem.giphy.model.GifModel
import fr.dev.majdi.netgem.giphy.model.ListGifModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiGif {

    //Api Random Gif
    @GET("/v1/gifs/random")
    fun getRandomGif(
        @Query("api_key") api_key: String,
        @Query("tag") tag: String,
        @Query("rating") rating: String
    ): Call<GifModel>

    //Api Search List of gif
    @GET("/v1/gifs/search")
    fun getSearchListOfGif(
        @Query("api_key") api_key: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("tag") tag: String,
        @Query("rating") rating: String,
        @Query("lang") lang: String
    ): Call<ListGifModel>

}