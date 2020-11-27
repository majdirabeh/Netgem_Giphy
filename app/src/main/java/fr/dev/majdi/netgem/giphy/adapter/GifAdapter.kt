package fr.dev.majdi.netgem.giphy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import fr.dev.majdi.netgem.giphy.R
import fr.dev.majdi.netgem.giphy.model.Data
import fr.dev.majdi.netgem.giphy.model.Gif
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gif_item.view.*

class GifAdapter() :
    RecyclerView.Adapter<GifAdapter.GifViewHolder>() {

    lateinit var onItemClickListener: ItemClickListener
    private var listGif: List<Data>

    init {
        listGif = listOf()
    }

    fun setOnclickListener(onItemClick: ItemClickListener) {
        onItemClickListener = onItemClick
    }

    fun setListGifData(listOfGif: List<Data>) {
        listGif = listOfGif
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = listGif[position]
        holder.bindView(gif)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(gif)
        }
    }

    override fun getItemCount(): Int {
        return listGif.size
    }

    inner class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(gif: Data) {
            Glide.with(itemView.context).asGif()
                .load(gif.images.preview_gif.url)
                .override(200, 200)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemView.findViewById(R.id.gifView))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    interface ItemClickListener {
        fun onItemClick(gif: Data)
    }

}