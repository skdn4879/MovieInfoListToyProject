package com.techtown.mymovie2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.techtown.mymovie2.moviedata.kobis.*
import com.techtown.mymovie2.moviedata.tmdb.TmdbMovieDetails
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.main_toolbar.*
import java.util.ArrayList

class MainActivity : AppCompatActivity(), FragmentCallBack {

    companion object{
        var requestQueue : RequestQueue? = null
    }

    val key = "d72c742c672c994be91a0516d459dd44"
    val dt = "20200615"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(applicationContext)

        setSupportActionBar(mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        menuDis.setOnClickListener {
            mainDrawerLayout.openDrawer(GravityCompat.START)
        }

        requestBoxOffice()

        //supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, Fragment1()).commit()

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bottomTab1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, Fragment1()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomTab2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, Fragment1()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomTab3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, Fragment1()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.bottomTab4 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, Fragment1()).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    override fun onBackPressed() {
        if(mainDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressed()
        }
    }

    override fun onFragmentSelected(item: FragmentCallBack.FragmentItem, bundle: Bundle?) {
        val index = bundle?.getInt("index", 0)

        val fragment: Fragment

        when(item){
            FragmentCallBack.FragmentItem.ITEM1 -> {
                mainToolbar.title = "영화목록"
                fragment = Fragment1()
            }
            FragmentCallBack.FragmentItem.ITEM2 -> {
                mainToolbar.title = "영화 상세"
                fragment = MovieDetailFragment.newInstance(index)
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
            }
        }
    }

    fun requestBoxOffice(){
        val url = "http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=${key}&targetDt=${dt}"

        val request = object : StringRequest(
            Request.Method.GET,
            url,
            {
                //키 사용 초과 여부 확인
                if(it.indexOf("faultInfo") > -1){
                    println("키 사용량 초과")
                }
                processResponse(it)
            },
            {
                println("\n에러 -> ${it.message}")
            }
        ) {}

        request.setShouldCache(false)
        requestQueue?.add(request)
    }

    fun processResponse(response: String){
        val gson = Gson()
        val boxOffice : BoxOffice = gson.fromJson(response, BoxOffice::class.java)

        requestDetails(boxOffice.boxOfficeResult.dailyBoxOfficeList)
    }

    fun requestDetails(dailyBoxOfficeList: ArrayList<MovieInfo>){
        MovieList.data.clear()
        for(index in 0..4){
            var movieData = MovieData(dailyBoxOfficeList[index], null, null)

            MovieList.data.add(movieData)

            sendDetails(index, dailyBoxOfficeList[index].movieCd)
        }
    }

    fun sendDetails(index: Int, movieCd: String?){
        if(movieCd != null){
            val url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=${key}&movieCd=${movieCd}"

            val request = object: StringRequest(
                Request.Method.GET,
                url,
                {

                    processDetailsResponse(index, it)
                },
                {
                    println("\n에러 -> ${it.message}")
                }
            ){}

            request.setShouldCache(false)
            requestQueue?.add(request)
        }
    }

    fun processDetailsResponse(index: Int, response: String){
        val gson = Gson()
        val movieInfoDetails: MovieInfoDetails = gson.fromJson(response, MovieInfoDetails::class.java)
        val movieInfoResult: MovieInfoResult = movieInfoDetails.movieInfoResult
        val movieDetails:MovieDetails = movieInfoResult.movieInfo
        MovieList.data[index].movieDetails = movieDetails

        requestTMDBSearch(index, movieDetails)
    }

    fun requestTMDBSearch(index: Int, movieDetails: MovieDetails){
        var movieName = movieDetails.movieNm
        val nation = movieDetails.nations[0].nationNm
        if(nation != "한국"){
            movieName = movieDetails.movieNmEn
        }

        sendTMDBSearch(index, movieName)
    }

    fun sendTMDBSearch(index: Int, movieName: String?){
        if(movieName != null){
            val apiKey = "2c8e5309eb865577b506eae508d6e513"
            val url = "https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${movieName}&language=ko-KR&page=1"

            val request = object: StringRequest(
                Request.Method.GET,
                url,
                {

                    processTMDBSearchResponse(index, it)
                },
                {
                    println("\n에러 -> ${it.message}")
                }
            ){}

            request.setShouldCache(false)
            requestQueue?.add(request)
        }
    }

    fun processTMDBSearchResponse(index: Int, response: String){
        val gson = Gson()
        val tmdbMovieDetails = gson.fromJson(response, TmdbMovieDetails::class.java)
        val movieResult = tmdbMovieDetails.results[0]

        MovieList.data[index].tmdbMovieResult = movieResult

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, Fragment1()).commit()
    }
}