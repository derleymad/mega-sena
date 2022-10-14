package com.github.derleymad.mega_sena.model.network

data class TesteItem(
    val acumuladaProxConcurso: String,
    val acumulou: Boolean,
    val concurso: Int,
    val data: String,
    val dataProxConcurso: String,
    val dezenas: List<String>,
    val estadosPremiados: List<EstadosPremiado>,
    val local: String,
    val loteria: String,
    val mesSorte: Any,
    val nome: String,
    val premiacoes: List<Premiacoe>,
    val proxConcurso: Int,
    val timeCoracao: Any
)