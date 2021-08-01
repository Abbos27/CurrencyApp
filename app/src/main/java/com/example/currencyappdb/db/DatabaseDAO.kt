package com.example.currencyappdb.db

import androidx.room.*
import com.example.currencyappdb.models.Currency

@Dao
interface DatabaseDAO {

    @Query("SELECT * FROM Currency ORDER BY num")
    fun getAllCurrenciesFromDb(): List<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCurrency(currency: Currency)


    @Query("UPDATE Currency SET date=:date, diff=:diff, rate=:rate, time=:time WHERE id = :id")
    fun updateCurrencyInDb(id: Int?, date:String? ,diff:String?,rate:String?,time:String?)

    @Query("DELETE FROM Currency WHERE id = :id")
    fun deleteCurrency(id: Int)

    @Query("SELECT id FROM Currency WHERE id = :id")
    fun hasTheSameCurrency(id: Int?): Boolean


}