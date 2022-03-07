package com.techtown.mymovie2

import android.view.View

interface OnMovieCommentClickListener {
    fun onItemClick(holder: MovieCommentAdapter.ViewHolder?, view: View?, position:Int)
}