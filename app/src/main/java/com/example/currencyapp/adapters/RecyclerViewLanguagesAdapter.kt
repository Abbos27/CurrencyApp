package com.example.currencyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.databinding.ItemLanguageBinding
import com.example.currencyapp.db.MySharedPreferences
import com.example.currencyapp.models.Language

class RecyclerViewLanguagesAdapter(private val langList: ArrayList<Language>, val listener: OnItemClick) :
    RecyclerView.Adapter<RecyclerViewLanguagesAdapter.ViewHolder>() {
    private val pref = MySharedPreferences()
    private lateinit var lastChosenView:ItemLanguageBinding

    inner class ViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(language: Language, position: Int) {
            pref.init(binding.root.context)
            binding.ivLang.setImageResource(language.image)
            binding.tvLang.text = language.langName
            binding.isLangChecked.isChecked = language.isChosen

            if(pref.checkedLangPosition == position) lastChosenView = binding

            binding.root.setOnClickListener {
                if (!binding.isLangChecked.isChecked) {
                    pref.checkedLangPosition = position
                    lastChosenView.isLangChecked.isChecked = false
                    binding.isLangChecked.isChecked = true
                    lastChosenView = binding

                    listener.onClick()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(langList[position], position)
    }


    override fun getItemCount(): Int = langList.size
}

interface OnItemClick {
    fun onClick()
}