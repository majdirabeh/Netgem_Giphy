package fr.dev.majdi.netgem.giphy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.dev.majdi.netgem.giphy.model.Data
import fr.dev.majdi.netgem.giphy.model.Gif
import fr.dev.majdi.netgem.giphy.network.ApiGif
import fr.dev.majdi.netgem.giphy.repository.MainRepository
import org.koin.standalone.KoinComponent

class MainViewModel(
    val mainRepository: MainRepository
) : ViewModel(), KoinComponent {

    var listGif = MutableLiveData<List<Data>>()
    var gifItem = MutableLiveData<Gif>()

    init {
        listGif.value = listOf()
        gifItem.value = null
    }

    /**
     * Get Random Gif from Repository
     */
    fun getRandomGif(
        apiKey: String,
        tag: String,
        rating: String
    ) {
        mainRepository.getRandomGif(
            apiKey,
            tag,
            rating,
            object : MainRepository.OnRandomGifInteraction {
                override fun onSuccess(gif: Gif) {
                    gifItem.value = gif
                }

                override fun onFailure() {
                    gifItem.value = null
                }

            })
    }

    /**
     * Search list of Gif from Repository
     */
    fun getSearchListOfGif(
        apiKey: String,
        query: String,
        limit: Int,
        offset: Int,
        tag: String,
        rating: String,
        lang: String
    ) {
        mainRepository.getSearchListOfGif(
            apiKey,
            query,
            limit,
            offset,
            tag,
            rating, lang,
            object : MainRepository.OnSearchListOfGifInteraction {
                override fun onSuccess(lisiGifs: List<Data>) {
                    listGif.value = lisiGifs
                }

                override fun onFailure() {
                    listGif.value = null
                }

            })
    }

}