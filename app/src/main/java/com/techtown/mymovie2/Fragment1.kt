package com.techtown.mymovie2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.techtown.mymovie2.moviedata.kobis.MovieList
import kotlinx.android.synthetic.main.fragment_1.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_1, container, false)
        rootView.pager.adapter = PagerAdapter(activity!!.supportFragmentManager, activity!!.lifecycle)
        rootView.pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        rootView.pager.clipToPadding = false
        rootView.pager.setPadding(150, 0, 150, 0)
        //rootView.pager.offscreenPageLimit = MovieList.data.size

        rootView.indicator.setViewPager(rootView.pager)
        rootView.indicator.createIndicators(MovieList.data.size, 0)

        rootView.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                rootView.indicator.animatePageSelected(position)
            }
        })
        return rootView
    }

    inner class PagerAdapter: FragmentStateAdapter {
        constructor(fm : FragmentManager, lc : Lifecycle) : super(fm, lc)

        override fun getItemCount() : Int {
            return MovieList.data.size
        }

        override fun createFragment(position : Int): Fragment {
            val movieData = MovieList.data[position]
            val title = movieData.movieInfo?.movieNm ?: ""
            val imageId = movieData.tmdbMovieResult?.poster_path ?:""
            val details1 = movieData.movieInfo?.audiCnt ?:""
            val details2 = movieData.movieDetails?.audits?.get(0)?.watchGradeNm ?:""

            val fragment = PageFragment.newInstance(imageId, title, details1, details2, position)

            return fragment
        }
    }
}