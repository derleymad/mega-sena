package com.github.derleymad.mega_sena.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.mega_sena.App
import com.github.derleymad.mega_sena.databinding.ActivityMainBinding
import com.github.derleymad.mega_sena.databinding.MyNumbersBinding
import com.github.derleymad.mega_sena.model.NumbersFav
import com.github.derleymad.mega_sena.ui.adapters.Adapter
import com.github.derleymad.mega_sena.ui.adapters.HistoryAdapter
import com.github.derleymad.mega_sena.ui.adapters.NumbersAdapter
import com.github.derleymad.mega_sena.utils.MAXCARTELA
import java.util.*


class MainActivity : AppCompatActivity(){
    private var listofNumbersFav = mutableListOf<NumbersFav>()
    private var myList = mutableListOf<Int>()

    private lateinit var binding : ActivityMainBinding
    private lateinit var bindingIncluded : MyNumbersBinding

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var numbersAdapter: NumbersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingIncluded = MyNumbersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //POJO MYLIST DEFAULT
        for (i in 1..6){
            myList.add(0)
        }

        //NUMBERS ADAPTER AND RECYCLER VIEW
        numbersAdapter = NumbersAdapter(myList)
        binding.rvNumbers.adapter = numbersAdapter
        binding.rvNumbers.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)

        //HISTORY ADAPTER AND RECYCLER VIEW
        historyAdapter = HistoryAdapter(listofNumbersFav)
        binding.rvHistory.adapter =  historyAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this@MainActivity)

        searchInDbAndUpdateHistoryAdapter()

        //CARTELA ADAPTER AND RECYCLER VIEW
        val adapter = Adapter(myList)
        binding.rvCartela.adapter = adapter
        binding.rvCartela.layoutManager = GridLayoutManager(this@MainActivity,10)

        binding.buttonMain.setOnClickListener{
            myList.clear()
            binding.rvCartela.adapter = adapter
            myList.addAll(generateNumbers())
            binding.rvNumbers.adapter?.notifyDataSetChanged()
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

            if (response.isEmpty()){
                binding.rvHistory.visibility = View.GONE
            }else{
                listofNumbersFav.clear()
                listofNumbersFav.addAll(response)
                runOnUiThread{
                    historyAdapter.notifyDataSetChanged()
                }
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
                binding.rvHistory.visibility = View.VISIBLE
            }
        }.start()
    }

    private fun generateNumbers() : List<Int> {
        val numbers = mutableSetOf<Int>()
        numbers.clear()
        val listOfNUmbers = mutableListOf<Int>()

        while (numbers.size != 6){
            val number = Random().nextInt(60) //de 0 a 59
            numbers.add((number+1))
        }
        listOfNUmbers.addAll(numbers)
        listOfNUmbers.sort()
//        val numbersString = numbers.joinToString("-")
        return listOfNUmbers
    }

}