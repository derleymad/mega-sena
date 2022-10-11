package com.github.derleymad.mega_sena.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.github.derleymad.mega_sena.App
import com.github.derleymad.mega_sena.R
import com.github.derleymad.mega_sena.databinding.ActivityMainBinding
import com.github.derleymad.mega_sena.databinding.MyNumbersBinding
import com.github.derleymad.mega_sena.model.NumbersFav
import com.github.derleymad.mega_sena.ui.adapters.Adapter
import com.github.derleymad.mega_sena.ui.adapters.HistoryAdapter
import java.util.*

class MainActivity : AppCompatActivity(){
    private lateinit var prefs:SharedPreferences

    private var listofNumbersFav = mutableListOf<NumbersFav>()
    private var myList = mutableListOf<Int>()

    private lateinit var binding : ActivityMainBinding
    private lateinit var bindingIncluded : MyNumbersBinding

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingIncluded = MyNumbersBinding.inflate(layoutInflater)

        listofNumbersFav = mutableListOf(NumbersFav(0,"teste","oxe"))
        historyAdapter = HistoryAdapter(listofNumbersFav)
        binding.rvHistory.adapter =  historyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this@MainActivity)

        searchInDbAndUpdateHistoryAdapter()

        val button = findViewById<Button>(R.id.button_main)


        prefs = getSharedPreferences("db_numbers", Context.MODE_PRIVATE)
//        txtResult.text = "Ultima aposta foi : " + prefs.getString("last-result",null)

        binding.rvCartela.visibility = View.VISIBLE
        val adapter = Adapter(myList)
        binding.rvCartela.adapter = adapter
        binding.rvCartela.layoutManager = GridLayoutManager(this@MainActivity,10)

        button.setOnClickListener{
            myList.clear() // LEMBRAR DA CARTELA QUE FICOU TODA VERMELHA
            myList.addAll(generateNumbers())
            binding.rvCartela.adapter?.notifyDataSetChanged()
        }

        binding.saveButton.setOnClickListener {
            insertInDbAndToastMessage()
        }
    }

    private fun searchInDbAndUpdateHistoryAdapter() {
        Thread{
            val app = application as App
            val dao = app.db.favDao()
            val response =  dao.getRegisterByType("sorteado")

            listofNumbersFav.clear()
            listofNumbersFav.addAll(response)

            runOnUiThread{
                historyAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun insertInDbAndToastMessage(){
        Thread{
            val app =  application as App
            val dao = app.db.favDao()
            val numbers = NumbersFav(numbers = myList.toString())

            dao.insert(
                numbers
            )

            runOnUiThread{
                Toast.makeText(applicationContext,"sucess",Toast.LENGTH_SHORT).show()
                listofNumbersFav.add(numbers)
                historyAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun generateNumbers() : List<Int> {
        val numbers = mutableSetOf<Int>()
        val list = mutableListOf<Int>()

        while (numbers.size != 6){
            val number = Random().nextInt(60) //de 0 a 59
            numbers.add((number+1))
        }
        list.addAll(numbers)
        list.sort()

//        val numbersString = numbers.joinToString("-")

        bindingIncluded.tv1.text = "${list[0].toString().padStart(2,'0')}"
        bindingIncluded.tv2.text="${list[1].toString().padStart(2,'0')}"
        bindingIncluded.tv3.text="${list[2].toString().padStart(2,'0')}"
        bindingIncluded.tv4.text="${list[3].toString().padStart(2,'0')}"
        bindingIncluded.tv5.text="${list[4].toString().padStart(2,'0')}"
        bindingIncluded.tv6.text="${list[4].toString().padStart(2,'0')}"

        return list
    }

}