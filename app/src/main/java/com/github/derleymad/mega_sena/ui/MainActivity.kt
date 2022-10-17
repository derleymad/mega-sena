package com.github.derleymad.mega_sena.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.derleymad.mega_sena.App
import com.github.derleymad.mega_sena.R
import com.github.derleymad.mega_sena.databinding.ActivityMainBinding
import com.github.derleymad.mega_sena.databinding.MyNumbersBinding
import com.github.derleymad.mega_sena.model.NumbersFav
import com.github.derleymad.mega_sena.model.network.ApiInterface
import com.github.derleymad.mega_sena.model.network.TesteItem
import com.github.derleymad.mega_sena.ui.adapters.Adapter
import com.github.derleymad.mega_sena.ui.adapters.HistoryAdapter
import com.github.derleymad.mega_sena.ui.adapters.NumbersAdapter
import com.github.derleymad.mega_sena.utils.MAXCARTELA
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MainActivity : AppCompatActivity(){
    private var listofNumbersFav = mutableListOf<NumbersFav>()
    private var myList = mutableListOf<Int>()
    private var myListLastResult = mutableListOf<String>()

    private lateinit var binding : ActivityMainBinding
    private lateinit var bindingIncluded : MyNumbersBinding

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var numbersAdapter: NumbersAdapter
    private lateinit var lastResultAdapter: NumbersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingIncluded = MyNumbersBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //POJO MYLIST DEFAULT
        for (i in 1..6){
            myList.add(0)
            myListLastResult.add("0")

        }

        //LAST RESULT ADAPTER AND RECYCLER VIEW
        getMyData()

        //NUMBERS ADAPTER AND RECYCLER VIEW
        numbersAdapter = NumbersAdapter(myList)
        binding.rvNumbers.adapter = numbersAdapter
        binding.rvNumbers.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)

        //HISTORY ADAPTER AND RECYCLER VIEW
        historyAdapter = HistoryAdapter(listofNumbersFav)
        binding.rvHistory.adapter =  historyAdapter
        var linearLayout = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,true)
        linearLayout.stackFromEnd = true
        binding.rvHistory.layoutManager =  linearLayout

        searchInDbAndUpdateHistoryAdapter()

        //CARTELA ADAPTER AND RECYCLER VIEW
        val adapter = Adapter(myList)
        binding.rvCartela.adapter = adapter
        binding.rvCartela.layoutManager = GridLayoutManager(this@MainActivity,10)

        binding.buttonMain.setOnClickListener{
            binding.rvNumbers.visibility = View.VISIBLE
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

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://loteriascaixa-api.herokuapp.com/")
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<TesteItem?> {
            override fun onResponse(call: Call<TesteItem?>, response: Response<TesteItem?>
            )
            {
                binding.progressBar.visibility = View.GONE
                binding.materialCardView.visibility = View.VISIBLE
                val response = response.body()!!
                lastResultAdapter = NumbersAdapter(response.dezenas.map{it.toInt()})
                binding.rvLastResult.adapter = lastResultAdapter
                binding.rvLastResult.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                binding.tvDate.text = response.data
                if(response.acumulou){
                    binding.tvAcumulado.text = "Acumulou " + response.acumuladaProxConcurso + " para o dia ${response.dataProxConcurso}"
                }else{
                    binding.tvAcumulado.text = "${response.premiacoes[0].premio} dividido com ${response.premiacoes[0].vencedores}"
                }
            }

            override fun onFailure(call: Call<TesteItem?>, t: Throwable)
            {
                Log.e("errorapi","nao foi")
                Toast.makeText(this@MainActivity,"Sem conexão com a internet!",Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.materialCardView.visibility = View.GONE
            }
        })
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

        if (myList.contains(0)){
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.no_data))
                .setMessage(getString(R.string.no_data_request_description))
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                }
                .create()
                .show()

        }else{
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.save_db))
                .setMessage(getString(R.string.save_request_description))
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    Thread{
                        val app =  application as App
                        val dao = app.db.favDao()
                        val numbers = NumbersFav(numbers = myList.toString())
                        dao.insert(numbers)
                        runOnUiThread{
                            Toast.makeText(applicationContext,"sucess",Toast.LENGTH_SHORT).show()
                            listofNumbersFav.add(numbers)
                            binding.rvHistory.adapter?.notifyItemInserted(listofNumbersFav.size-1)
                            binding.rvHistory.scrollToPosition(listofNumbersFav.size-1)
                            binding.rvHistory.visibility = View.VISIBLE
                        }
                    }.start()
                }
                .setNegativeButton(android.R.string.cancel) { _, _ ->
                }
                .create()
                .show()




        }
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