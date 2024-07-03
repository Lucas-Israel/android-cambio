package com.betrybe.currencyview.ui.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrybe.currencyview.ui.adapters.LatestRatesAdapter
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiServiceClient.instance
    private val currMenu: AutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val selCurTexView: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }
    private val currRatesViewer: RecyclerView by lazy { findViewById(R.id.currency_rates_state) }
    private val mainCurrState: MaterialTextView by lazy { findViewById(R.id.load_currency_state) }
    private val waitingResp: FrameLayout by lazy { findViewById(R.id.waiting_response_state) }

    // init
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currMenu.setOnItemClickListener { parent, _view, position, _id ->
            selCurTexView.visibility = View.GONE
            currRatesViewer.visibility = View.VISIBLE
            waitingResp.visibility = View.VISIBLE
            
            val currTitle = parent.getItemAtPosition(position) as String

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    ApiIdlingResource.increment()
                    val ratesResponse = apiService.getLatestRates(currTitle)
                    val rates = ratesResponse.body()?.rates?.toList().orEmpty()
                    val adapter = LatestRatesAdapter(rates)

                    withContext(Dispatchers.Main) {
                        currRatesViewer.adapter = adapter
                        currRatesViewer.layoutManager = LinearLayoutManager(baseContext)

                        adapter.notifyDataSetChanged()
                    }

                    ApiIdlingResource.decrement()
                } catch (e: HttpException) {
                    ApiIdlingResource.decrement()
                } catch (e: IOException) {
                    ApiIdlingResource.decrement()
                }

            }
            waitingResp.visibility = View.GONE

        }

    }

    override fun onStart() {
        super.onStart()
        mainCurrState.visibility = View.VISIBLE
        selCurTexView.visibility = View.VISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                val symbolsResponse = apiService.getSymbols()
                val symbols = symbolsResponse.body()
                val symbolsTitles = symbols?.symbols?.keys?.toList().orEmpty()
                val adapter =
                    ArrayAdapter(baseContext, android.R.layout.simple_list_item_1, symbolsTitles)

                withContext(Dispatchers.Main) {
                    currMenu.setAdapter(adapter)
                }

                ApiIdlingResource.decrement()
            } catch (e: IOException) {
                ApiIdlingResource.decrement()
            }
        }
        mainCurrState.visibility = View.GONE
    }
}
