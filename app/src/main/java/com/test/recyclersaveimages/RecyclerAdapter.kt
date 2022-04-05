package com.test.recyclersaveimages

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.recyclersaveimages.model.Data


class RecyclerAdapter(val data: Data, val mainActivity: MainActivity) :RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var title = itemView.findViewById<TextView>(R.id.title)
        var cv = itemView.findViewById<CardView>(R.id.cv_hold)
        var ivImage = itemView.findViewById<ImageView>(R.id.iv_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_recyler,parent,false)
        return ViewHolder(view)    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.cv.setOnClickListener {
            var intent = Intent(mainActivity, ImageViewActivity::class.java)
            intent.putExtra("title",data[position].title)
//            intent.putExtra("url",data[position].url)
            intent.putExtra("url","https://reqres.in/img/faces/7-image.jpg")
            mainActivity.startActivity(intent)
        }
        Glide.with(mainActivity).load(data[position].url)
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        Log.d("asd",data.size.toString())
        return data.size
    }
}