package com.techtown.mymovie2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_comment_item.view.*
import java.util.ArrayList

class MovieCommentAdapter : RecyclerView.Adapter<MovieCommentAdapter.ViewHolder>() {

    var items = ArrayList<MovieComment>()

    lateinit var listener:OnMovieCommentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.movie_comment_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init{
            itemView.setOnClickListener {
                listener?.onItemClick(this, itemView, adapterPosition)
            }
        }

        fun setItem(item:MovieComment){
            itemView.idTextView.text = item.id.toString()
            itemView.dateTextView.text = item.date.toString()
            //itemView.ratingBar.rating = item.rating?.toFloat()?:0.0f
            itemView.ratingBar.rating = 4.5f
            itemView.contentsTextView.text = item.contents.toString()
            itemView.recommandCountTextView.text = item.recommendCount
        }
    }
}