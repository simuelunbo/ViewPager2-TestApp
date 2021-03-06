package com.example.slideviewer

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainAdapter : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {

    private val list = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    fun addList(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }


    inner class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val ivBanner: ImageView = v.findViewById(R.id.iv_banner)
        private val tvName: TextView = v.findViewById(R.id.tv_name)
        fun bind(list: ArrayList<String>) {
            val index = adapterPosition % list.size
            val item = list[index]
            tvName.text = String.format("%s", item)
            if (adapterPosition % 2 == 0) {
                ivBanner.setBackgroundColor(Color.RED)
            } else {
                ivBanner.setBackgroundColor(Color.BLUE)
            }

        }
    }
}