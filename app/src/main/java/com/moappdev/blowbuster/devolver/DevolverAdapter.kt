package com.moappdev.blowbuster.devolver

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.ItemPrestadoBinding
import com.moappdev.blowbuster.ejemplares.EjemplaresAdaptar


class DevolverAdapter(val click: ClickListenerEjemplar):ListAdapter<EjemplarDb, DevolverAdapter.ViewHolder>(
    EjemplaresAdaptar.EjemplaresDiffCallBack()
) {
    class ViewHolder private constructor(val binding:ItemPrestadoBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item: EjemplarDb, click: ClickListenerEjemplar){
            binding.ejemplar=item
            binding.click=click
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                var binding= ItemPrestadoBinding.inflate(layoutInflater, parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class ClickListenerEjemplar(val click: (ejemplar:EjemplarDb)->Unit) {
        fun onCLick(ejemplar: EjemplarDb)=click(ejemplar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),click)
    }
}