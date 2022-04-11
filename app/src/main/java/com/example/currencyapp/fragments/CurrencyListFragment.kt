package com.example.currencyapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.currencyapp.R
import com.example.currencyapp.adapters.RecyclerViewAdapter
import com.example.currencyapp.databinding.FragmentCurrencyListBinding
import com.example.currencyapp.models.Currency
import com.example.currencyapp.models.Flag
import com.example.currencyapp.models.TimeModel
import com.example.currencyapp.networking.NetworkHelper
import com.example.currencyapp.networking.RetrofitBuilder
import com.mynameismidori.currencypicker.ExtendedCurrency
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrencyListFragment : Fragment() {
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding

    private lateinit var adapter: RecyclerViewAdapter
    private var tempListCurrencies: ArrayList<Currency> = arrayListOf()
    private var flags: ArrayList<Flag> = arrayListOf()
    private lateinit var networkHelper: NetworkHelper


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)

        adapter = RecyclerViewAdapter()
        binding?.rvCurrency?.adapter = adapter

        generateSomeFlags()
        loadData()

        binding?.refreshRv?.setOnRefreshListener {
            loadData()
        }

        binding?.btnUpdate?.setOnClickListener {
            binding?.progressBar?.visibility = View.VISIBLE
            loadData()
        }

        return binding?.root
    }


    override fun onResume() {
        super.onResume()
        if (checkConnection()) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun checkConnection(): Boolean {
        networkHelper = NetworkHelper(activity?.applicationContext!!)
        return networkHelper.isConnected()
    }

    private fun loadData() {
        if (checkConnection()) {
            binding?.btnUpdate?.visibility = View.GONE
            binding?.progressBar?.visibility = View.GONE
            binding?.messageNoConnection?.visibility = View.GONE
            binding?.rvCurrency?.visibility = View.VISIBLE
            getTimeApi()
        } else {
            binding?.refreshRv?.setRefreshing(false)
            binding?.rvCurrency?.visibility = View.GONE
            binding?.btnUpdate?.visibility = View.VISIBLE
            binding?.messageNoConnection?.visibility = View.VISIBLE
        }
    }

    private fun getCurrencyApi() {
        RetrofitBuilder.api.getAllCurrencies().enqueue(object : Callback<List<Currency>> {
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>
            ) {
                if (response.isSuccessful) {
                    tempListCurrencies = (response.body() as ArrayList<Currency>?)!!

                    setFlags()
                    adapter.differ.submitList(tempListCurrencies)
                    adapter.shimmerStatus(false)
                    binding?.refreshRv?.setRefreshing(false)
                }
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {

            }
        })
    }

    private fun getTimeApi() {
        RetrofitBuilder.apiTime.getTime().enqueue(object : Callback<TimeModel> {
            override fun onResponse(call: Call<TimeModel>, response: Response<TimeModel>) {
                if (response.isSuccessful) {
                    setTime(response.body())
                    getCurrencyApi()
                }
            }

            override fun onFailure(call: Call<TimeModel>, t: Throwable) {
                getCurrencyApi()
            }
        })
    }

    private fun setFlags() {
        tempListCurrencies.forEach { t ->
            t.time = Currency.onlineTime
            var b = true
            flags.forEach { f ->
                if (t.ccy.equals(f.ccy)) {
                    t.flag = f.flag
                    b = false
                }
            }
            if (b) t.flag = R.drawable.ic_globe

        }

    }

    private fun setTime(time: TimeModel?) {
        var hours = "${time?.hours}"
        var minutes = "${time?.minutes}"
        if (hours.toInt() < 10) hours = "0${time?.hours}"
        if (minutes.toInt() < 10) minutes = "0${time?.minutes}"

        Currency.onlineTime = hours + ":" + minutes
    }

    private fun generateSomeFlags() {
        flags = arrayListOf<Flag>()
        val currencies: List<ExtendedCurrency> = ExtendedCurrency.getAllCurrencies()
        currencies.forEach {
            flags.add(Flag(it.code, it.flag))
        }
        flags.add(Flag("AMD", R.drawable.amd))
        flags.add(Flag("AZN", R.drawable.azn))
        flags.add(Flag("BYN", R.drawable.byn))
        flags.add(Flag("CUP", R.drawable.cup))
        flags.add(Flag("DZD", R.drawable.dzd))
        flags.add(Flag("IQD", R.drawable.iqd))
        flags.add(Flag("IRR", R.drawable.irr))
        flags.add(Flag("JOD", R.drawable.jod))
        flags.add(Flag("KGS", R.drawable.kgs))
        flags.add(Flag("KHR", R.drawable.khr))
        flags.add(Flag("LBP", R.drawable.lbp))
        flags.add(Flag("LYD", R.drawable.lyd))
        flags.add(Flag("MMK", R.drawable.mmk))
        flags.add(Flag("SYP", R.drawable.syp))
        flags.add(Flag("VES", R.drawable.ves))
        flags.add(Flag("TJS", R.drawable.tjs))
        flags.add(Flag("TND", R.drawable.tnd))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}