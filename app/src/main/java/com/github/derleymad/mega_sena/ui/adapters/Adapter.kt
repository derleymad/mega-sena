package com.github.derleymad.mega_sena.ui.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.derleymad.mega_sena.R

const val MAXCARTELA = 60

class Adapter(
//    private val list : List<Int>,
    private val myList: List<Int>): RecyclerView.Adapter<Adapter.ViewHolder>(){
    private val list = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.number_item,parent,false)
        for(i in 1..MAXCARTELA){
            list.add(i)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemCurrent = list[position]
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return MAXCARTELA
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(itemCurrent:Int){

            val number = itemView.findViewById<TextView>(R.id.number_item_tv)
            if (myList.contains(itemCurrent)){
                number.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_700))
                number.setTypeface(null,Typeface.BOLD)
            }
            number.text = "[${itemCurrent.toString().padStart(2,'0')}]"
        }
    }
}
