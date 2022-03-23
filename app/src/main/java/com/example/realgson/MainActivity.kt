package com.example.realgson

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realgson.domain.data.Photo
import com.example.realgson.domain.data.PhotoPage
import com.example.realgson.domain.data.Wrapper
import com.example.realgson.domain.holder.CatAdapter
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.setHasFixedSize(true)

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        recyclerView.adapter = CatAdapter(this, getLinks(), clipboardManager)
    }

    private fun getLinks(): List<Photo> {
        var photos = ArrayList<Photo>() as List<Photo>

        CoroutineScope(Dispatchers.IO).launch {
            val connection = withContext(Dispatchers.IO) {
                URL(getString(R.string.link)).openConnection()
            } as HttpsURLConnection

            val json = connection.inputStream.bufferedReader().readText()

            val wrapper = Gson().fromJson(json, Wrapper::class.java)
            val photoPage = Gson().fromJson(wrapper.page, PhotoPage::class.java)
            photos = Gson().fromJson(photoPage.photo, Array<Photo>::class.java).toList()
        }

        return photos
    }
}