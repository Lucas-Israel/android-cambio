package com.betrybe.currencyview.ui.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.betrybe.currencyview.common.ApiIdlingResource
import com.betrybe.currencyview.data.api.ApiServiceClient
import com.betrye.currencyview.R
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val apiService = ApiServiceClient.instance
    private val currMenu: AutoCompleteTextView by lazy { findViewById(R.id.currency_selection_input_layout) }
    private val selCurTexView: MaterialTextView by lazy { findViewById(R.id.select_currency_state) }

    // init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        currMenu.setOnItemClickListener { parent, view, position, id ->
            val currTitle = parent.getItemAtPosition(position) as String

            Log.i("App ---", currTitle)
        }
        
    }

    override fun onStart() {
        super.onStart()

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
    }
}
