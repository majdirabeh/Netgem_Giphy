package fr.dev.majdi.netgem.giphy

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.dev.majdi.netgem.giphy.model.Gif
import fr.dev.majdi.netgem.giphy.network.ApiGif
import fr.dev.majdi.netgem.giphy.repository.MainRepository
import fr.dev.majdi.netgem.giphy.utils.Constants
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.API_KEY
import fr.dev.majdi.netgem.giphy.viewmodel.MainViewModel
import junit.framework.Assert.assertEquals
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {


    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    private lateinit var viewModel: MainViewModel
    private lateinit var api: ApiGif
    private lateinit var mainRepository: MainRepository
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        api = createWebService()
        mainRepository = MainRepository(api)
        viewModel = MainViewModel(mainRepository)


        mockWebServer = MockWebServer()
        mockWebServer.start()


    }

    fun createWebService(): ApiGif {
        //val interceptor = HttpLoggingInterceptor()
        //interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client: OkHttpClient = OkHttpClient.Builder().build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .build()

        return retrofit.create(ApiGif::class.java)
    }

    @Test
    fun `fetch random gif and check response Code 200 returned`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)


        val actualResponse = api.getRandomGif(
            API_KEY,
            "",
            "g"
        ).execute()

        //Assert
        assertEquals(
            response.toString().contains("200"),
            actualResponse.code().toString().contains("200")
        )

    }

    @Test
    fun `fetch random gif result and check response success returned`() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(response)
        val mockResponse = response.getBody()?.readUtf8()
        val actualResponse = api.getRandomGif(
            API_KEY,
            "",
            "g"
        ).execute()


        assertEquals(mockResponse?.let { `parse mocked JSON response`(it) }, actualResponse.body()?.meta!!.status)
    }

    private fun `parse mocked JSON response`(mockResponse: String): Int {
        val reader = JSONObject(mockResponse)
        return reader.getJSONObject("meta").getInt("status")
    }

    @Test
    fun `fetch details for failed response 400 returned`(){
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(MockResponseFileReader("failed_response.json").content)
        mockWebServer.enqueue(response)
        // Act
        val  actualResponse = api.getRandomGif(
            "API_KEY",
            "",
            "g"
        ).execute()
        // Assert
        assertEquals(response.toString().contains("Invalid authentication credentials"),actualResponse.toString().contains("Invalid authentication credentials"))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

}