package com.example.currencyappdb.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.currencyappdb.models.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase : RoomDatabase() {

    abstract fun currencyDao(): DatabaseDAO

    companion object {
        @Volatile
        private var instance: CurrencyDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CurrencyDatabase::class.java,
            "currency_db.db"
        ).allowMainThreadQueries().build()

    }

}