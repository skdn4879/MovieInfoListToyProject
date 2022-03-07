package com.techtown.mymovie2

import android.os.Bundle
import android.view.LayoutInflater
import com.techtown.mymovie2.MovieDetailFragment

interface FragmentCallBack {
    enum class FragmentItem{
        ITEM1, ITEM2, ITEM3
    }

    fun onFragmentSelected(item: FragmentItem, bundle: Bundle?)
}