package com.example.currencyapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R
import com.example.currencyapp.databinding.ConverterDialogBinding
import com.example.currencyapp.databinding.ItemCurrencyBinding
import com.example.currencyapp.db.CurrencyDatabase
import com.example.currencyapp.db.MySharedPreferences
import com.example.currencyapp.models.Currency
import com.like.LikeButton
import com.like.OnLikeListener
import java.util.*


class RecyclerViewAdapter :
    RecyclerView.Adapter<RecyclerViewAdapter.VH>() {
    private var isShimmer = true
    private var shimmerSize = 7
    private val pref = MySharedPreferences()


    private val itemCallback = object : DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, itemCallback)


    inner class VH(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val dao = CurrencyDatabase.invoke(binding.root.context).currencyDao()

        fun onBind(currency: Currency?) {
            pref.init(binding.root.context)
            binding.shimmerFrameLayout.stopShimmer()
            binding.shimmerFrameLayout.setShimmer(null)
            binding.tvTitle.background = null
            binding.tvRate.background = null
            binding.updatedDate.background = null
            binding.chosenCurrency.visibility = View.VISIBLE

            when {
                pref.chosenLang.equals("uz") -> setTitle(currency?.ccyNmUZ)
                pref.chosenLang.equals("ru") -> setTitle(currency?.ccyNmRU)
                else -> setTitle(currency?.ccyNmEN)
            }


            binding.tvRate.text = "${currency?.nominal} ${currency?.ccy} = ${currency?.rate} UZS"
            binding.updatedDate.text =
                binding.root.context.getString(R.string.updated_date_text) + " " + currency?.date + " | " + currency?.time
            binding.chosenCurrency.isLiked = dao.hasTheSameCurrency(currency?.id)
            binding.imageCountry.setImageResource(currency?.flag!!)



            if (binding.chosenCurrency.isLiked) {
                dao.updateCurrencyInDb(
                    currency.id,
                    currency.date,
                    currency.diff,
                    currency.rate,
                    currency.time
                )
            }

            binding.chosenCurrency.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    dao.addCurrency(currency)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    dao.deleteCurrency(currency.id!!)
                }

            })

            binding.root.setOnClickListener {
                openConverterDialog(binding.root.context, currency, binding.tvTitle.text.toString())
            }
        }

        private fun setTitle(str: String?) {
            binding.tvTitle.text = str
        }

        fun onShimmerActive() {
            binding.shimmerFrameLayout.startShimmer()
            binding.chosenCurrency.visibility = View.GONE

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (isShimmer) holder.onShimmerActive()
        else holder.onBind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return if (isShimmer) shimmerSize
        else differ.currentList.size

    }

    fun shimmerStatus(isShimmer: Boolean) {
        this.isShimmer = isShimmer
        notifyDataSetChanged()
    }

    private fun openConverterDialog(context: Context, currency: Currency, title: String) {
        pref.isChanged = false
        val dialog = AlertDialog.Builder(context).create()
        val view = ConverterDialogBinding.inflate(LayoutInflater.from(context), null, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var b = true
        var titleTop = title
        var titleBottom = when {
            pref.chosenLang.equals("ru") -> "Узбекский сум"
            pref.chosenLang.equals("uz") -> "O`zbekiston so`mi"
            else -> "Uzbekistan Sum"
        }

        changeCurrency(view, titleTop, currency.flag!!, currency.ccy, titleBottom, R.drawable.ic_uzb, "UZS")

        view.fab.setOnClickListener {
            pref.isChanged = !pref.isChanged
            calculateCurrency(view, currency.rate!! )
            b = !b
            val handler = Handler()
            if(b) {
                rotateAnimation(view,0f, 180f)
                disappearanceAnim(view)

                val handler = Handler()
                handler.postDelayed(Runnable {
                    changeCurrency(view, titleTop, currency.flag!!, currency.ccy, titleBottom, R.drawable.ic_uzb, "UZS")
                    appearanceAnim(view)
                }, 500)
            }
            else {
                rotateAnimation(view,0f, 180f)
                disappearanceAnim(view)

                handler.postDelayed(Runnable {
                    changeCurrency(view, titleBottom, R.drawable.ic_uzb, "UZS", titleTop, currency.flag!!, currency.ccy!!)
                    appearanceAnim(view)
                }, 500)
            }
        }

        view.etTop.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                calculateCurrency(view, currency.rate!! )
            }
        })

        dialog.setView(view.root)
        dialog.show()
    }

    private fun calculateCurrency(view: ConverterDialogBinding, rate: String) {
        if (view.etTop.text.toString().endsWith(".")) {
            var k = 0
            view.etTop.text.toString().forEach {
                if (it == '.') k++
            }
            if (k > 1) {
                view.etTop.setText(
                    view.etTop.text.toString().substring(
                        0,
                        view.etTop.text.toString().length - 1
                    )
                )
                view.etTop.setSelection(view.etTop.text.toString().length)
            }
        }
        if (view.etTop.text.toString().startsWith(".")) {
            view.etTop.setText("0.")
            view.etTop.setSelection(2)
        }
        if ((view.etTop.text.toString()
                .startsWith("0") && view.etTop.text.toString().length > 1 && view.etTop.text.toString()
                .substring(
                    1,
                    2
                ) != ".")
        ) {
            view.etTop.setText(
                view.etTop.text.toString().substring(
                    1,
                    view.etTop.text.toString().length
                )
            )
            view.etTop.setSelection(view.etTop.text.toString().length)
        }

        if (view.etTop.text.toString().isNotEmpty()) {
            var k = 0
            view.etTop.text.toString().forEachIndexed { i, c ->
                if (c == '.') {
                    k++
                    if (k > 1) {
                        view.etTop.setText(view.etTop.text.toString().substring(0, i))
                        view.etTop.setSelection(i)
                    }
                }
            }

            if (!view.etTop.text.toString().endsWith(".")) {
                var value:Double? = null
                value = if(pref.isChanged){
                    (view.etTop.text.toString().toDouble()) / (rate.toDouble()!!)
                }else{
                    (view.etTop.text.toString().toDouble()) * (rate.toDouble()!!)
                }
                view.etBottom.text = String.format(Locale.CANADA, "%.2f", value)
            }
        } else {
            view.etBottom.text = ""
        }
    }

    private fun rotateAnimation(view: ConverterDialogBinding, fromDegrees: Float, toDegrees: Float) {
        val rotateAnimation = RotateAnimation(fromDegrees,toDegrees,RotateAnimation.RELATIVE_TO_SELF,.5f,RotateAnimation.RELATIVE_TO_SELF, .5f)
        rotateAnimation.duration = 400
        view.fab.startAnimation(rotateAnimation)
    }

    private fun appearanceAnim(view: ConverterDialogBinding) {
        view.rvCurrencyTitleTop.startAnimation(setTextAnimation(view, R.anim.title_appearance))
        view.rvCurrencyTitleBottom.startAnimation(setTextAnimation(view, R.anim.title_appearance))

        view.tvTop.startAnimation(setTextAnimation(view, R.anim.top_appearance))
        view.ivTop.startAnimation(setTextAnimation(view, R.anim.top_appearance))
        view.ivBgTop.startAnimation(setTextAnimation(view, R.anim.top_appearance))

        view.tvBottom.startAnimation(setTextAnimation(view, R.anim.bottom_appearance))
        view.ivBottom.startAnimation(setTextAnimation(view, R.anim.bottom_appearance))
        view.ivBgBottom.startAnimation(setTextAnimation(view, R.anim.bottom_appearance))
    }

    private fun disappearanceAnim(view: ConverterDialogBinding) {
        view.rvCurrencyTitleTop.startAnimation(setTextAnimation(view, R.anim.title_disappearance))
        view.rvCurrencyTitleBottom.startAnimation(setTextAnimation(view, R.anim.title_disappearance))

        view.tvTop.startAnimation(setTextAnimation(view, R.anim.top_disappearance))
        view.ivTop.startAnimation(setTextAnimation(view, R.anim.top_disappearance))
        view.ivBgTop.startAnimation(setTextAnimation(view, R.anim.top_disappearance))

        view.tvBottom.startAnimation(setTextAnimation(view, R.anim.bottom_disappearance))
        view.ivBottom.startAnimation(setTextAnimation(view, R.anim.bottom_disappearance))
        view.ivBgBottom.startAnimation(setTextAnimation(view, R.anim.bottom_disappearance))
    }

    private fun changeCurrency(view: ConverterDialogBinding, titleTop: String, flagTop: Int, ccyTop: String?, titleBottom: String, flagBottom: Int, ccyBottom: String) {
        view.rvCurrencyTitleTop.text = titleTop
        view.ivTop.setImageResource(flagTop)
        view.tvTop.text = ccyTop

        view.rvCurrencyTitleBottom.text = titleBottom
        view.ivBottom.setImageResource(flagBottom)
        view.tvBottom.text = ccyBottom
    }

    private fun setTextAnimation(view: ConverterDialogBinding, animationRes: Int): Animation? {
        return AnimationUtils.loadAnimation(view.root.context, animationRes)
    }



}

