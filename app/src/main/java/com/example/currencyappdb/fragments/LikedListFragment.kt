package com.example.currencyappdb.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.currencyappdb.adapters.RecyclerViewAdapter
import com.example.currencyappdb.databinding.FragmentLikedListBinding
import com.example.currencyappdb.db.CurrencyDatabase


class LikedListFragment : Fragment() {
    private var _binding: FragmentLikedListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter:RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikedListBinding.inflate(inflater, container, false)
        adapter = RecyclerViewAdapter()
        binding.rvLikedList.adapter = adapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val currencyDao = CurrencyDatabase.invoke(binding.root.context).currencyDao()
        adapter.differ.submitList(currencyDao.getAllCurrenciesFromDb())
        adapter.notifyDataSetChanged()
        adapter.shimmerStatus(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}