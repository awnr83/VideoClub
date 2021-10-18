package com.moappdev.blowbuster.sala

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.databinding.FragmentSalaBinding
import java.lang.IllegalArgumentException

class SalaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding= FragmentSalaBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        val viewModelFactory= SalaViewModelFactory(db)
        val viewModel= ViewModelProvider(this, viewModelFactory).get(SalaViewModel::class.java)
        binding.viewmodel=viewModel
        binding.lifecycleOwner=this

        val adapter= SalaAdapter(SalaAdapter.ClickVhsSala {
            viewModel.estadoVhs(it)
        })
        binding.rbVhs.adapter=adapter
        viewModel.vhs.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root
    }
}

class SalaViewModelFactory(private val db: VideoClubDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SalaViewModel::class.java))
            return  SalaViewModel(db) as T
        throw IllegalArgumentException("Error al crear el Viewmodel")
    }

}
