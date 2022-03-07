package com.techtown.mymovie2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.techtown.mymovie2.moviedata.kobis.MovieList
import kotlinx.android.synthetic.main.fragment_movie_detail.view.*
import kotlinx.android.synthetic.main.fragment_page.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if(bundle != null){
            index = bundle.getInt("index")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false)

        setData(rootView)

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rootView.recyclerView.layoutManager = layoutManager

        val adapter = MovieCommentAdapter()

        adapter.items.add(MovieComment("sky2", "1분전", "5.0", "개노잼", "5"))
        adapter.items.add(MovieComment("sky3", "10분전", "4.5", "핵노잼", "3"))
        adapter.items.add(MovieComment("sky4", "100분전", "4.8", "시발노잼", "7"))

        rootView.recyclerView.adapter = adapter

        adapter.listener = object : OnMovieCommentClickListener{
            override fun onItemClick(
                holder: MovieCommentAdapter.ViewHolder?,
                view: View?,
                position: Int
            ) {
                val name = adapter.items[position]
                showToast("아이템 선택됨: ${name}")
            }
        }

        return rootView
    }

    fun showToast(message:String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val TAG = "MovieDetailFragment"

        fun newInstance(index: Int?) : MovieDetailFragment{
            val fragment = MovieDetailFragment()

            val bundle = Bundle()
            bundle.putInt("index", index!!)
            fragment.arguments = bundle

            return fragment
        }
    }

    fun setData(rootView:View){
        val movieData = MovieList.data[index]

        //포스터 이미지
        val imageId = movieData.tmdbMovieResult?.poster_path
        if(imageId != null && imageId!!.isNotEmpty()){
            val url = "http://image.tmdb.org/t/p/w200${imageId}"

            Glide.with(this).load(url).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true).dontAnimate().into(rootView.thumbnailView)
        }

        //타이틀
        rootView.movieTitleView.text = movieData.movieInfo?.movieNm

        //관람등급
        val gradeTitle = movieData.movieDetails?.audits?.get(0)?.watchGradeNm
        var grade = 0
        if(gradeTitle != null){
            if(gradeTitle.indexOf("전체") > -1){
                grade = 0
                rootView.ratingView.setImageResource(R.drawable.ic_all)
            } else if(gradeTitle.indexOf("12") > -1){
                grade = 1
                rootView.ratingView.setImageResource(R.drawable.ic_12)
            } else if(gradeTitle.indexOf("15") > -1){
                grade = 2
                rootView.ratingView.setImageResource(R.drawable.ic_15)
            } else if(gradeTitle.indexOf("19") > -1){
                grade = 3
                rootView.ratingView.setImageResource(R.drawable.ic_19)
            }
        }

        //개봉일
        rootView.releaseDateView.text = movieData.movieInfo?.openDt

        //장르
        rootView.genreView.text = movieData.movieDetails?.genres?.get(0)?.genreNm

        //상영시간
        rootView.runningTimeView.text = movieData.movieDetails?.showTm

        //순위
        rootView.rankingView.text = "${index + 1}위"

        //관객수
        rootView.marketShareView.text = movieData.movieInfo?.audiCnt

        //감독
        rootView.directorView.text = movieData.movieDetails?.directors?.get(0)?.peopleNm

        //배우
        rootView.actorView.text = movieData.movieDetails?.actors?.get(0)?.peopleNm + "(" +
                movieData.movieDetails?.actors?.get(0)?.cast + ")"

        //영화사
        rootView.companyView.text = movieData.movieDetails?.companys?.get(0)?.companyNm

        //줄거리
        rootView.synopsisView.text = movieData.tmdbMovieResult?.overview
    }
}