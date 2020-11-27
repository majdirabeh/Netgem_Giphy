package fr.dev.majdi.netgem.giphy.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.ferfalk.simplesearchview.SimpleSearchView
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.ui.pagination.GPHContent
import fr.dev.majdi.netgem.giphy.R
import fr.dev.majdi.netgem.giphy.adapter.GifAdapter
import fr.dev.majdi.netgem.giphy.base.BaseActivity
import fr.dev.majdi.netgem.giphy.model.Data
import fr.dev.majdi.netgem.giphy.network.ApiGif
import fr.dev.majdi.netgem.giphy.utils.Constants
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.API_KEY
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.LANG
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.LIMIT
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.OFFSET
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.RATING
import fr.dev.majdi.netgem.giphy.utils.Constants.Companion.TAG
import fr.dev.majdi.netgem.giphy.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


@Suppress("JAVA_CLASS_ON_COMPANION")
class MainActivity : BaseActivity(), GifAdapter.ItemClickListener {

    companion object {
        val TAG = MainActivity.javaClass::class.simpleName
    }

    //Inject MainViewModel
    private val mainViewModel: MainViewModel by viewModel()

    private val gifAdapter = GifAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intiView()
        loadRandomGif()
    }

    private fun loadRandomGif(){
        mainViewModel.getRandomGif(API_KEY, Constants.TAG, RATING)
        mainViewModel.gifItem.observe(this, Observer {
            if (it != null) {
                Glide.with(this).asGif()
                    .load(it.gifModel.data.images.downsized_large.url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .apply(RequestOptions().override(200, 200))
                    .into(randomGif)
                setVisibilityProgress(false, progress)
            }
        })
    }

    private fun intiView(){
        setVisibilityProgress(true, progress)
        initToolbar(toolbar)
        initRecycleView(gifGridView)
        dismissKeyboard(rootView)
        setVisibilityGridLayout(false, randomListGifLayout, randomGifLayout)
        searchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewShown() {

            }

            override fun onSearchViewClosed() {
                setVisibilityGridLayout(false, randomListGifLayout, randomGifLayout)
                setVisibilityProgress(false, progress)
            }

            override fun onSearchViewShownAnimation() {

            }

            override fun onSearchViewClosedAnimation() {

            }

        })
        searchView.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) = Unit

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                performSearch(p0.toString())
            }
        })
    }

    private fun performSearch(query: String) {
        mainViewModel.getSearchListOfGif(API_KEY, query, LIMIT, OFFSET, Constants.TAG, RATING, LANG)
        mainViewModel.listGif.observe(this, Observer {
            if (it.isNotEmpty()) {
                gifAdapter.setListGifData(it)
                gifAdapter.setOnclickListener(this)
                gifGridView.adapter = gifAdapter
                setVisibilityGridLayout(true, randomListGifLayout, randomGifLayout)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        setupSearchView(menu!!)
        return true
    }

    override fun onBackPressed() {
        if (searchView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onItemClick(gif: Data) {
        val bundle = Bundle()
        bundle.putString("urlGif", gif.images.downsized_large.url)
        val fullScreenDialog = FullScreenDialog()
        fullScreenDialog.arguments = bundle
        //Show dialog when click item
        fullScreenDialog.show(supportFragmentManager, "FullScreenDialog")
    }


}