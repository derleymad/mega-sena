package com.github.derleymad.mega_sena.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.mega_sena.R
import com.github.derleymad.mega_sena.model.NumbersFav

class HistoryAdapter(val list : List<NumbersFav>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.history_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCurrent = list[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bind(itemCurrent: NumbersFav){
            val numbers = view.findViewById<TextView>(R.id.tv_static_history_item)
            numbers.text = itemCurrent.numbers
        }
    }
}