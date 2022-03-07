package com.techtown.mymovie2.moviedata.kobis

import java.util.ArrayList

data class BoxOfficeResult(val boxofficeType : String?,
                           val showRange : String?,
                           val dailyBoxOfficeList: ArrayList<MovieInfo> = ArrayList<MovieInfo>()
)
