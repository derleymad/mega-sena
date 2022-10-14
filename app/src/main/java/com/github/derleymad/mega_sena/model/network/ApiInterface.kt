package com.github.derleymad.mega_sena.model.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api/mega-sena/latest")
    fun getData() : Call<TesteItem>
}