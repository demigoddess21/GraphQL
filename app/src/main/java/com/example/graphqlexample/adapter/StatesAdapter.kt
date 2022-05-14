package com.example.graphqlexample.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graphqlexample.CountryDetailsQuery
import com.example.graphqlexample.databinding.ContinentsListBinding
import com.example.graphqlexample.databinding.StatesListBinding

class StatesAdapter(
    private val states: List<CountryDetailsQuery.State>,
    private val listener: (CountryDetailsQuery.State) -> Unit
) :
    RecyclerView.Adapter<StatesAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val teaBinding =
            StatesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(teaBinding, parent)
    }

    override fun getItemCount(): Int {

        return states.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.bindTo(states[position]) { listener(states[position]) }



    }

    class ViewHolder(val binding: StatesListBinding, val parent: ViewGroup) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(states: CountryDetailsQuery.State, listener: View.OnClickListener) {
            binding.apply {
                rootView.setOnClickListener(listener)

                tvContinent.setText(states.name)


            }

        }
    }





}