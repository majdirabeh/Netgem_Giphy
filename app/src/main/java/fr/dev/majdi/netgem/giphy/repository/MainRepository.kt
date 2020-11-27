package fr.dev.majdi.netgem.giphy.repository

import android.content.Context
import com.giphy.sdk.ui.GPHSettings
import com.giphy.sdk.ui.Giphy
import fr.dev.majdi.netgem.giphy.model.Data
import fr.dev.majdi.netgem.giphy.model.Gif
import fr.dev.majdi.netgem.giphy.model.GifModel
import fr.dev.majdi.netgem.giphy.model.ListGifModel
import fr.dev.majdi.netgem.giphy.network.ApiGif
import retrofit2.Call
import retrofit2.Response

class MainRepository(
    private val apiGif: ApiGif
) {
    /**
     * Get Random Gif from Api
     */
    fun getRandomGif(
        apiKey: String,
        tag: String,
        rating: String,
        onRandomGifInteraction: OnRandomGifInteraction
    ) {
        apiGif.getRandomGif(apiKey, tag, rating).enqueue(object : retrofit2.Callback<GifModel> {
            override fun onResponse(call: Call<GifModel>, response: Response<GifModel>) {
                val gif = Gif(response.body()!!)
                onRandomGifInteraction.onSuccess(gif)
            }

            override fun onFailure(call: Call<GifModel>, t: Throwable) {
                onRandomGifInteraction.onFailure()
            }

        })
    }

    /**
     * Get List of Gif from Api
     */
    fun getSearchListOfGif(
        apiKey: String,
        query: String,
        limit: Int,
        offset: Int,
        tag: String,
        rating: String,
        lang: String,
        onSearchListOfGifInteraction: OnSearchListOfGifInteraction
    ) {
        apiGif.getSearchListOfGif(apiKey, query, limit, offset, tag, rating,lang)
            .enqueue(object : retrofit2.Callback<ListGifModel> {
                override fun onResponse(
                    call: Call<ListGifModel>,
                    response: Response<ListGifModel>
                ) {
                    val listGifModel = arrayListOf<Data>()
                    if (response.body() != null) {
                        listGifModel.addAll(response.body()!!.data)
                        onSearchListOfGifInteraction.onSuccess(listGifModel)
                    }

                }

                override fun onFailure(call: Call<ListGifModel>, t: Throwable) {
                    onSearchListOfGifInteraction.onFailure()
                }

            })
    }

    interface OnRandomGifInteraction {
        fun onSuccess(gif: Gif)
        fun onFailure()
    }

    interface OnSearchListOfGifInteraction {
        fun onSuccess(lisiGifs: List<Data>)
        fun onFailure()
    }

}