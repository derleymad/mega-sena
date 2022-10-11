package com.github.derleymad.mega_sena.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NumbersFav::class], version = 1)
//Padrão Singleton esse objeto aki em baixo vai ser ÚNICO pois será armazenado em memória...

abstract class AppDatabase : RoomDatabase() {
    abstract fun favDao(): FavDao

    companion object{
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return if(INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "numbers_db"
                    ).build()
                }
                INSTANCE as AppDatabase
            }else{
                INSTANCE as AppDatabase
            }
        }
    }
}