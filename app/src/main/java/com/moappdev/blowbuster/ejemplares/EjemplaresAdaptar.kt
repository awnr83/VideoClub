package com.moappdev.blowbuster.ejemplares

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.ItemEjemplarBinding


class EjemplaresAdaptar(val click: ClickListener,val clickLong: ClickLongListener):ListAdapter<EjemplarDb, EjemplaresAdaptar.ViewHolder>(EjemplaresDiffCallBack()) {
    class ViewHolder private constructor(val binding:ItemEjemplarBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: EjemplarDb, _click: ClickListener, _clickLong: ClickLongListener){

            binding.apply {
                ejemplar=item
                tvPublicacion.text=item.publicacion.toString()
                click=_click
                clickLong=_clickLong
                executePendingBindings()
            }
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                var binding= ItemEjemplarBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class EjemplaresDiffCallBack:DiffUtil.ItemCallback<EjemplarDb>() {
        override fun areItemsTheSame(oldItem: EjemplarDb, newItem: EjemplarDb): Boolean {
            return oldItem===newItem
        }
        override fun areContentsTheSame(oldItem: EjemplarDb, newItem: EjemplarDb): Boolean {
            return oldItem.idSocio==newItem.idSocio
        }

    }

    class ClickListener(val clickListener:(Ejemplar: EjemplarDb)->Unit) {
        fun onClick(ejemplar: EjemplarDb)= clickListener(ejemplar)
    }

    class ClickLongListener(val clickLongListener:(Ejemplar: EjemplarDb)->Unit) {
        fun onClickLong(ejemplar: EjemplarDb):Boolean{
            clickLongListener(ejemplar)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjemplaresAdaptar.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EjemplaresAdaptar.ViewHolder, position: Int) {
        holder.bind(getItem(position),click,clickLong)
    }

}