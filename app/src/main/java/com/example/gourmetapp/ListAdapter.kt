package com.example.gourmetapp

import android.annotation.SuppressLint
import android.app.LauncherActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(private val itemList: List<ListItem>, private  val onItemClickListener: OnItemClickListener) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val photoImageView: ImageView = itemView.findViewById(R.id.image)
        val textTextView1: TextView = itemView.findViewById(R.id.name)
        val textTextView2: TextView = itemView.findViewById(R.id.access)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val item = itemList[position]
                onItemClickListener.onItemClick(item)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: ListItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.photoImageView.setImageBitmap(item.photo)
        holder.textTextView1.text = item.name
        holder.textTextView2.text = item.access
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
