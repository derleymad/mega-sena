package com.github.derleymad.mega_sena.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NumbersFav(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "type") val type : String = "sorteado",
    @ColumnInfo(name = "numbers") val numbers: String = ""
)
