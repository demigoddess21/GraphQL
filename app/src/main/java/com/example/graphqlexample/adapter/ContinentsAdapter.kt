package com.example.graphqlexample.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlexample.ContinentsQuery
import com.example.graphqlexample.databinding.ContinentsListBinding

class ContinentsAdapter(
    private val continents: List<ContinentsQuery.Continent>?,
    private val listener: (ContinentsQuery.Continent) -> Unit
) :
RecyclerView.Adapter<ContinentsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val teaBinding =
            ContinentsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(teaBinding, parent)
    }

    override fun getItemCount(): Int {

        return continents!!.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bindTo(continents!![position]) { listener(continents[position]) }



    }

    class ViewHolder(val binding: ContinentsListBinding, val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(continent: ContinentsQuery.Continent, listener: View.OnClickListener) {
            binding.apply {
                rootView.setOnClickListener(listener)
                tvContinent.setText(continent.name)
                val countryCount = continent.countries.size

                if(countryCount > 11){
                    tvCount.setText("+11")
                }
                else{
                    tvCount.setText(continent.countries.size.toString())
                }

               Log.e("TAG", "COUNTRIES: "+continent.countries.size.toString())

            }

        }
    }





}