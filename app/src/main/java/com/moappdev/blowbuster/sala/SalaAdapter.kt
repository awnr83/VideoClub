package com.moappdev.blowbuster.sala

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.ItemVhsBinding


class SalaAdapter(val click: ClickVhsSala): ListAdapter<EjemplarDb, SalaAdapter.ViewHolder>(SalaDiffUtil()) {
    class ViewHolder private constructor(val binding: ItemVhsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: EjemplarDb, click: ClickVhsSala){
            binding.ejemplar=item
            binding.click= click
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater= LayoutInflater.from(parent.context)
                val binding= ItemVhsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class SalaDiffUtil:DiffUtil.ItemCallback<EjemplarDb>() {
        override fun areItemsTheSame(oldItem: EjemplarDb, newItem: EjemplarDb): Boolean {
            return oldItem===newItem
        }

        override fun areContentsTheSame(oldItem: EjemplarDb, newItem: EjemplarDb): Boolean {
            return oldItem.codigo==newItem.codigo
        }

    }

    class ClickVhsSala(val click:(ejemplar: EjemplarDb)->Unit) {
        fun onClick(ejemplar: EjemplarDb)=click(ejemplar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }
}