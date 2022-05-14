package com.example.graphqlexample.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlexample.CountriesListQuery
import com.example.graphqlexample.databinding.CountriesListBinding

class CountriesAdapter(
    private val continents: List<CountriesListQuery.Country>?,
    private val listener: (CountriesListQuery.Country) -> Unit
) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val teaBinding =
            CountriesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(teaBinding, parent)
    }

    override fun getItemCount(): Int {

        return continents!!.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bindTo(continents!![position]) { listener(continents[position]) }



    }

    class ViewHolder(val binding: CountriesListBinding, val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(countries: CountriesListQuery.Country, listener: View.OnClickListener) {
            binding.apply {
                rootView.setOnClickListener(listener)
                tvCountry.setText(countries.name)
                val countryCount = countries.states.size

                if(countryCount > 11){
                    tvCount.setText("+11")
                }
                else{
                    tvCount.setText(countries.states.size.toString())
                }

                Log.e("TAG", "COUNTRIES: "+countries.states.size.toString())

            }

        }
    }





}