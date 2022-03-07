package com.techtown.mymovie2.moviedata.kobis

import com.techtown.mymovie2.moviedata.tmdb.MovieResult

data class MovieData(var movieInfo: MovieInfo?,
                     var movieDetails : MovieDetails?,
                     var tmdbMovieResult : MovieResult?)
