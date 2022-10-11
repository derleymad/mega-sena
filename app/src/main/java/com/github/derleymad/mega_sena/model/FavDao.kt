package com.github.derleymad.mega_sena.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

    @Dao
    interface FavDao {
        @Insert
        fun insert(fav: NumbersFav)

        @Delete
        fun delete(fav: NumbersFav)

        @Query("SELECT * FROM NumbersFav WHERE type = :type")
        fun getRegisterByType(type:String) : List<NumbersFav>

    }