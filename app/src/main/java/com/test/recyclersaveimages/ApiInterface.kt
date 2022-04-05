package com.test.recyclersaveimages

import android.graphics.Movie
import com.test.recyclersaveimages.model.Data
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface ApiInterface {

    @GET("photos")
    fun getMovies() : Call<Data>

    companion object {

        var BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create() : ApiInterface {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}