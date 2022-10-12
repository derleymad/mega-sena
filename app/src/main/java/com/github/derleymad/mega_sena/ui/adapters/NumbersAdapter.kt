package com.github.derleymad.mega_sena.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.mega_sena.R

class NumbersAdapter(private val list : List<Int>) : RecyclerView.Adapter<NumbersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.my_numbers,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCurrent = list[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        fun bind(itemCurrent: Int){
            val tvNumber = itemView.findViewById<TextView>(R.id.tv_number_item)
            tvNumber.text = "${itemCurrent.toString().padStart(2,'0')}"
        }
    }
}