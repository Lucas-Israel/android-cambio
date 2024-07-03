package com.betrybe.currencyview.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView

class LatestRatesAdapter(private val rates: List<Pair<String, Double>>) : RecyclerView.Adapter<LatestRatesAdapter.LatestRatesViewHolder>() {

    class LatestRatesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val base: MaterialTextView = view.findViewById(R.id.rate_title_name)
        val value: MaterialTextView = view.findViewById(R.id.rate_value)
    }

    override fun getItemCount() = rates.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestRatesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rates_card, parent, false)

        return LatestRatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: LatestRatesViewHolder, position: Int) {
        holder.base.text = rates[position].first
        holder.value.text = rates[position].second.toString()
    }
}