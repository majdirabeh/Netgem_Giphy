package fr.dev.majdi.netgem.giphy.base

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ferfalk.simplesearchview.utils.DimensUtils
import com.giphy.sdk.ui.GiphyLoadingProvider
import fr.dev.majdi.netgem.giphy.R
import fr.dev.majdi.netgem.giphy.utils.Constants
import fr.dev.majdi.netgem.giphy.utils.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

open class BaseActivity : AppCompatActivity() {

    //Init Toolbar
    fun initToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    //Setup SearchView
    fun setupSearchView(menu: Menu) {
        val item = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)
        val revealCenter: Point = searchView.revealAnimationCenter
        revealCenter.x -= DimensUtils.convertDpToPx(Constants.EXTRA_REVEAL_CENTER_PADDING, this)
    }
    //Set visibility of list when click search button
    fun setVisibilityGridLayout(setVisible: Boolean, viewGrid: View, viewGif: View) {
        if (setVisible) {
            viewGrid.visibility = View.VISIBLE
            viewGif.visibility = View.GONE
        } else {
            viewGrid.visibility = View.GONE
            viewGif.visibility = View.VISIBLE
        }
    }
    fun setVisibilityProgress(setVisible: Boolean, progress: View) {
        if (setVisible) {
            progress.visibility = View.VISIBLE
        } else {
            progress.visibility = View.GONE
        }
    }
    //Dismiss keyBoard
    fun dismissKeyboard(view: View) {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            // TODO: handle exception
            e.printStackTrace()
        }
    }
    //Init GridView gif
    fun initRecycleView(recycler: RecyclerView) {
        val layoutManager = GridLayoutManager(this, 2)
        val gridSpacingItemDecoration = GridSpacingItemDecoration(2, 2,false, 0)
        recycler.layoutManager = layoutManager
        recycler.addItemDecoration(gridSpacingItemDecoration)
        recycler.setHasFixedSize(true)

    }

}