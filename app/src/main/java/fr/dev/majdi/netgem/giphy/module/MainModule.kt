package fr.dev.majdi.netgem.giphy.module

import fr.dev.majdi.netgem.giphy.network.ApiGif
import fr.dev.majdi.netgem.giphy.repository.MainRepository
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.BASE_URL
import fr.dev.majdi.netgem.giphy.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {
    single { createWebService() }
    single { MainRepository(get()) }
    viewModel { MainViewModel(get()) }
}

/**
 * Init Retrofit
 */
fun createWebService(): ApiGif {
    val interceptor = HttpLoggingInterceptor()
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

    return retrofit.create(ApiGif::class.java)
}