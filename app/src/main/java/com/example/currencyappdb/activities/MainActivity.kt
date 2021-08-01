package com.example.currencyappdb.activities

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.example.currencyappdb.R
import com.example.currencyappdb.adapters.OnItemClick
import com.example.currencyappdb.adapters.RecyclerViewLanguagesAdapter
import com.example.currencyappdb.adapters.ViewPagerAdapter
import com.example.currencyappdb.databinding.ActivityMainBinding
import com.example.currencyappdb.databinding.LangListDialogBinding
import com.example.currencyappdb.db.MySharedPreferences
import com.example.currencyappdb.fragments.CurrencyListFragment
import com.example.currencyappdb.fragments.LikedListFragment
import com.example.currencyappdb.models.Language
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val pref = MySharedPreferences()
    private lateinit var toolbar: Toolbar
    private lateinit var switcher: CheckBox
    private lateinit var changeLangIc: ImageView
    private lateinit var langText: TextView
    private lateinit var cv: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        pref.init(this)
        setLocale(pref.chosenLang!!)
        setContentView(binding.root)


        toolbar = findViewById(R.id.toolbar)
        switcher = findViewById(R.id.switcher)
        changeLangIc = findViewById(R.id.ic_change_lang)
        langText = findViewById(R.id.lang_text)
        cv = findViewById(R.id.cv)

        cv.setOnClickListener {
            showChangeLangDialog()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        loadLangIc()
        loadTheme()
        loadThemeIcon()

        val fragments = listOf(CurrencyListFragment(), LikedListFragment())
        val titles = listOf(
            getString(R.string.exchange_rates_text),
            getString(R.string.chosen_rates_text),
        )

        binding.viewPager.adapter = ViewPagerAdapter(fragments, titles, supportFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)


        switcher.setOnClickListener {
            pref.isDay = !pref.isDay
            loadTheme()
        }


    }

    private fun loadThemeIcon() {
        if (pref.isDay) switcher.setButtonDrawable(R.drawable.ic_sun)
        else switcher.setButtonDrawable(R.drawable.ic_moon)
    }

    private fun loadTheme() {
        if (pref.isDay) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun loadLangIc() {
        langText.text = getString(R.string.lang_text)
        when {
            pref.chosenLang.equals("ru") -> changeLangIc.setImageResource(R.drawable.ic_rus)
            pref.chosenLang.equals("uz") -> changeLangIc.setImageResource(R.drawable.ic_uzb)
            else -> changeLangIc.setImageResource(R.drawable.ic_usa)
        }
    }

    private fun showChangeLangDialog() {
        val langList = arrayListOf(
            Language(R.drawable.ic_uzb, "O`zbekcha"),
            Language(R.drawable.ic_rus, "Русский"),
            Language(R.drawable.ic_usa, "English")
        )
        langList[pref.checkedLangPosition].isChosen = true

        val dialog = AlertDialog.Builder(this).create()
        val view = LangListDialogBinding.inflate(layoutInflater, null, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(view.root)

        view.rvLang.adapter = RecyclerViewLanguagesAdapter(langList, object: OnItemClick{
            override fun onClick() {
                itemLangClicked()
                dialog.dismiss()
            }

        })
        dialog.show()
    }

    private fun itemLangClicked() {
        when (pref.checkedLangPosition) {
            0 -> {
                if (!pref.chosenLang.equals("uz")) {
                    setLocale("uz")
                    recreate()
                }
            }
            1 -> {
                if (!pref.chosenLang.equals("ru")) {
                    setLocale("ru")
                    recreate()
                }
            }
            2 -> {
                if (!pref.chosenLang.equals("en")) {
                    setLocale("en")
                    recreate()
                }
            }
        }

    }

    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        pref.chosenLang = lang
    }


}