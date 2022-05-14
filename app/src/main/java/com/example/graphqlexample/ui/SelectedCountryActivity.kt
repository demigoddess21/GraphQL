package com.example.graphqlexample.ui

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.example.graphqlexample.CountryDetailsQuery

import com.example.graphqlexample.adapter.StatesAdapter
import com.example.graphqlexample.apollo.ApolloInstance
import com.example.graphqlexample.databinding.ActivitySelectedCountryBinding
import org.json.JSONArray
import java.io.IOException
import com.caverock.androidsvg.SVG
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.graphqlexample.R


class SelectedCountryActivity : AppCompatActivity() {

    private lateinit var cname: String
    private lateinit var svg: SVG
    private var flag: String = ""
    private lateinit var binding: ActivitySelectedCountryBinding
    private lateinit var client: ApolloClient
    private lateinit var statesAdapter: StatesAdapter
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivitySelectedCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = ApolloInstance().get()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.rvData.layoutManager = mLayoutManager
        getCountryDetails()
    }

    private fun getCountryDetails() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val countryCode = sharedPreferences.getString("countryCode","")
        val countryName = sharedPreferences.getString("countryName","")
        Log.e("TAG","CONTINENT CODE: "+countryCode)
        lifecycleScope.launchWhenResumed {
            val response = try {
                client.query(CountryDetailsQuery(countryCode!!)).execute()
            }catch (e : ApolloException){
                return@launchWhenResumed
            }
            val countryDetails = response.data?.country

            if (countryDetails == null || response.hasErrors()) {
                Log.e("TAG","RESPONSE: "+countryDetails)
                return@launchWhenResumed
            }
            else {
                binding.toolbar.title.setText(countryDetails.name)
                binding.toolbar.ivBack.setOnClickListener {
                    onBackPressed()
                }
                var data = getJsonDataFromAsset(this@SelectedCountryActivity,"countrydata.json")
                var countrydata= JSONArray(data)
                for (l in 0..countrydata.length()-1)
                {
                    var cdata=countrydata.getJSONObject(l)
                    cname = cdata.getString("name")
                    if(countryDetails.name.contains(cname)) {
                        flag = cdata.getString("flag")
                    }
                }

                  Log.e("TAG","FLAG: "+flag)
                    if (flag != null) {
                        binding.imgCountryFlag.loadUrl(flag)
                    }





                binding.tvCode.text = countryDetails.code
                binding.tvNative.text = countryDetails.native

                if(countryDetails.capital.isNullOrEmpty()){
                    binding.tvCapital.text = "-"
                }
                else{
                    binding.tvCapital.text = countryDetails.capital
                }



                if(countryDetails.states.isNullOrEmpty()){
                    binding.tvNoData.visibility = View.VISIBLE
                    binding.rvData.visibility = View.GONE
                }
                else{
                    binding.tvNoData.visibility = View.GONE
                    binding.rvData.visibility = View.VISIBLE
                    statesAdapter = StatesAdapter(countryDetails.states) {
                        // onSelectOutlet(it)
                    }
                    binding.rvData.adapter = statesAdapter
                }


            }
        }
    }
    fun ImageView.loadUrl(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }
    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}