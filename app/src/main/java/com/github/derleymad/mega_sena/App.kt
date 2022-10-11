package com.github.derleymad.mega_sena

import android.app.Application
import com.github.derleymad.mega_sena.model.AppDatabase

class App : Application(){
    lateinit var db : AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = AppDatabase.getDatabase(this@App)
    }
}