package com.example.currencyapp.db

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences {
    private val NAME = "MyPref"
    private val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isDay: Boolean
        get() = preferences.getBoolean("isDay", true)
        set(value) = preferences.edit {
            it.putBoolean("isDay", value)
        }

    var chosenLang: String?
        get() = preferences.getString("lang", "ru")
        set(value) = preferences.edit {
            it.putString("lang", value)
        }


    var checkedLangPosition: Int
        get() = preferences.getInt("position", 1)
        set(value) = preferences.edit {
            it.putInt("position", value)
        }

    var langPosition: Int
        get() = preferences.getInt("lang_pos", 1)
        set(value) = preferences.edit {
            it.putInt("lang_pos", value)
        }



    var isChanged: Boolean
        get() = preferences.getBoolean("is_changed", false)
        set(value) = preferences.edit {
            it.putBoolean("is_changed", value)
        }


}

