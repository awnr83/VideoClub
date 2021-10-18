package com.moappdev.blowbuster.socios

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.databinding.ItemSocioBinding


class SocioAdapter(val clickPhone: ClickPhone, val clickDelete: ClickDelete,val clickSocio: ClickSocio): ListAdapter<SocioDb, SocioAdapter.ViewHolder>(SocioDiffCallBack()) {

    class ViewHolder private constructor(val binding: ItemSocioBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SocioDb,clickPhone: ClickPhone, clickDelete: ClickDelete, clickSocio: ClickSocio){
            binding.socio=item
            binding.phone=clickPhone
            binding.delete=clickDelete
            binding.clickSocio=clickSocio
            binding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater= LayoutInflater.from(parent.context)
                var binding= ItemSocioBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    class SocioDiffCallBack:DiffUtil.ItemCallback<SocioDb>() {
        override fun areItemsTheSame(oldItem: SocioDb, newItem: SocioDb): Boolean {
            return oldItem===newItem
        }
        override fun areContentsTheSame(oldItem: SocioDb, newItem: SocioDb): Boolean {
            return oldItem.idSocio==newItem.idSocio
        }

    }

    class ClickPhone(val clickPhone: (item: SocioDb)->Unit) {
        fun onClick(item: SocioDb)=clickPhone(item)
    }

    class ClickDelete(val clickDelete:(item:SocioDb)->Unit) {
        fun onClick(item: SocioDb)=clickDelete(item)
    }

    class ClickSocio(val clickSocio:(socio:SocioDb)->Unit) {
        fun onClick(item: SocioDb)=clickSocio(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioAdapter.ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SocioAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position),clickPhone,clickDelete,clickSocio)
    }
}