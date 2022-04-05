package com.test.recyclersaveimages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.recyclersaveimages.model.Data
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val apiInterface = ApiInterface.create().getMovies()
        context = this
        checkperm()



        apiInterface.enqueue( object : Callback<Data> {
            override fun onResponse(call: Call<Data>?, response: Response<Data>?) {

                if(response?.body() != null){
                    Log.d("asd",response.body().toString())
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerView.adapter = RecyclerAdapter(response.body()!!,this@MainActivity)
                }


            }


            override fun onFailure(call: Call<Data>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
    companion object{
         lateinit var context : MainActivity
    }

    fun checkperm() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                123
            )
            Toast.makeText(this@MainActivity,"Need permission",Toast.LENGTH_LONG)
        }
    }
}