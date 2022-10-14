package com.github.derleymad.mega_sena.model.network

data class EstadosPremiado(
    val cidades: List<Cidade>,
    val latitude: String,
    val longitude: String,
    val nome: String,
    val uf: String,
    val vencedores: String
)