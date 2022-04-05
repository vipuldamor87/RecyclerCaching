package com.test.recyclersaveimages

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_image_view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

class ImageViewActivity : AppCompatActivity() {
    lateinit var fileName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        var mImage: Bitmap?

        var url = intent.getStringExtra("url")
        fileName = url!!.substring(url!!.lastIndexOf('/') + 1, url!!.length)

        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val file = File(imagesDir, fileName)
        if (file.exists()) {
            Glide.with(this)
                .load(file) // Uri of the picture
                .into(ivasd);
        } else {

            val myExecutor = Executors.newSingleThreadExecutor()
            val myHandler = Handler(Looper.getMainLooper())
            myExecutor.execute {
                mImage = url?.let { mLoad(it) }
                myHandler.post {
                    ivasd.setImageBitmap(mImage)
                    if (mImage != null) {
                        mSaveMediaToStorage(mImage)
                    }
                }
            }
        }

    }
    // Function to establish connection and load image

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    // Function to save image on the device.
    // Refer: https://www.geeksforgeeks.org/circular-crop-an-image-and-save-it-to-the-file-in-android/
    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
//        val filename = "${System.currentTimeMillis()}.jpg"
        val filename = fileName
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }
}