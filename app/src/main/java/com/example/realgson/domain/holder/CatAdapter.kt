package com.example.realgson.domain.holder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.example.realgson.R
import com.example.realgson.domain.data.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.HttpURLConnection
import java.net.URL
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties


class CatAdapter(private val context: Context, private val list: List<Photo>, private val clipboardManager: ClipboardManager)
    : RecyclerView.Adapter<CatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatHolder = CatHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
    )

    override fun onBindViewHolder(holder: CatHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val link = compileLink(list[position])

            try {
                val connection = withContext(Dispatchers.IO) {
                    URL(link).openConnection()
                } as HttpURLConnection

                holder.image.setImageBitmap(BitmapFactory.decodeStream(connection.inputStream))
            } catch (error: Exception) {
                Timber.e(error)
            }

            holder.itemView.setOnClickListener {
                link.copyLinkToClipBoard()
            }
        }

    }

    override fun getItemCount(): Int = list.size

    private fun compileLink(photo: Photo) : String {
        var source = context.getString(R.string.download_url)

        for (property in Photo::class.memberProperties)
            source = source.replace(Regex("\\{${property.name}}"),
                readInstanceProperty(photo, property.name))

        return source
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> readInstanceProperty(instance: T, propertyName: String): String {
        val property = instance!!::class.members
            .first { it.name == propertyName } as KProperty1<Any, *>
        return property.get(instance).toString()
    }

    private fun String.copyLinkToClipBoard() {
        val data = ClipData.newPlainText("text", this)
        clipboardManager.setPrimaryClip(data)
        Timber.i(this)
    }
}