package com.example.realgson.domain.holder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.realgson.R

class CatHolder(view: View) : ViewHolder(view) {
    val image = view.findViewById(R.id.image_view) as ImageView
}