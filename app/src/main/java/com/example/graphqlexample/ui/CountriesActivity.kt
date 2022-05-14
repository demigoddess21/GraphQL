package com.example.graphqlexample.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.example.graphqlexample.CountriesListQuery
import com.example.graphqlexample.adapter.CountriesAdapter
import com.example.graphqlexample.apollo.ApolloInstance
import com.example.graphqlexample.databinding.ActivityCountriesBinding

class CountriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCountriesBinding
    private lateinit var client: ApolloClient
    private lateinit var continentAdapter: CountriesAdapter
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCountriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = ApolloInstance().get()

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.toolbar.title.setText("Countries")
        binding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.rvData.layoutManager = mLayoutManager


        getCountriesList()
    }

    private fun getCountriesList() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val continentCode = sharedPreferences.getString("continentCode","")
        Log.e("TAG","CONTINENT CODE: "+continentCode)
        lifecycleScope.launchWhenResumed {
            val response = try {
                client.query(CountriesListQuery(continentCode!!)).execute()
            }catch (e : ApolloException){
                return@launchWhenResumed
            }
            val continents = response.data?.continent?.countries

            if (continents == null || response.hasErrors()) {
                Log.e("TAG","RESPONSE: "+continents)
                return@launchWhenResumed
            }
            else {

                continentAdapter = CountriesAdapter(continents) {
                    onSelectOutlet(it)
                }


                binding.rvData.adapter = continentAdapter

            }
        }
    }

    private fun onSelectOutlet(country: CountriesListQuery.Country) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("countryCode",country.code)
        editor.putString("countryName",country.code)
        editor.apply()
        editor.commit()
        Log.e("TAG","SELECTED COUNTRY: "+country.name)
        val intent = Intent(this,SelectedCountryActivity::class.java)
        startActivity(intent)
    }
}