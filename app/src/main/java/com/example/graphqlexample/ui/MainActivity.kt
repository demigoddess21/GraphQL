package com.example.graphqlexample.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.example.graphqlexample.ContinentsQuery
import com.example.graphqlexample.adapter.ContinentsAdapter
import com.example.graphqlexample.apollo.ApolloInstance
import com.example.graphqlexample.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var client: ApolloClient
    private lateinit var continentAdapter: ContinentsAdapter
    private val sharedPrefFile = "kotlinsharedpreference"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        client = ApolloInstance().get()

        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvData.layoutManager = mLayoutManager


//        binding.btnAdd.setOnClickListener {
//            val add = Intent(this, CountriesActivity::class.java)
//            add.action = "add"
//            activityResult.launch(add)
//        }

        getUsersList()
    }

    private fun getUsersList() {
        lifecycleScope.launchWhenResumed {
            val response = try {
                client.query(ContinentsQuery()).execute()
            }catch (e : ApolloException){
                return@launchWhenResumed
            }



            val continents = response.data?.continents

            if (continents == null || response.hasErrors()) {
                Log.e("TAG","RESPONSE: "+continents)
                return@launchWhenResumed
            }
            else {

                continentAdapter = ContinentsAdapter(continents) {
                    onSelectOutlet(it)
                }


                binding.rvData.adapter = continentAdapter
//                if (arrayList.isEmpty()) {
//                    binding.tvLoading.visibility = View.VISIBLE
//                    binding.tvLoading.text = "resources.getString(R.string.no_data)"
//                } else {
//                    binding.tvLoading.visibility = View.GONE
//                }
            }
        }
    }

    private fun onSelectOutlet(continents: ContinentsQuery.Continent) {
        val sharedPreferences: SharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString("continentCode",continents.code)
        editor.apply()
        editor.commit()
//        val bundle = Bundle()
//        bundle.putParcelable("continent", continents)
        val intent = Intent(this,CountriesActivity::class.java)
        startActivity(intent)
    }



    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                getUsersList()
            }
        }
}


