package com.moappdev.blowbuster.devolver

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.FragmentDevolverBinding
import com.moappdev.blowbuster.ejemplares.EjemplaresFragment
import com.moappdev.blowbuster.prestar.PrestarViewModel
import java.lang.IllegalArgumentException

class DevolverFragment : Fragment() {

    private lateinit var mBinding: FragmentDevolverBinding
    private lateinit var mViewmodel: DevolverViewModel
    private lateinit var mAdapter: DevolverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentDevolverBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        val viewModelFactory= DevolverViewModelFactory(db)
        mViewmodel= ViewModelProvider(this,viewModelFactory).get(DevolverViewModel::class.java)
        mBinding.viewmodel= mViewmodel
        mBinding.lifecycleOwner=this

        mAdapter=DevolverAdapter(DevolverAdapter.ClickListenerEjemplar {
            mostarDialogo(it)
        })
        mBinding.rvPrestados.adapter=mAdapter
        mViewmodel.prestados.observe(viewLifecycleOwner, Observer {
            it?.let {
                mAdapter.submitList(it)
            }
        })

        return mBinding.root
    }

    private fun mostarDialogo(ejemplar: EjemplarDb) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Devolder Ejemplar?")
            .setIcon(R.drawable.ic_devolver)
            .setMessage("Titulo: ${ejemplar.nombre}\nCodigo: ${ejemplar.codigo}\nNro de socio: ${ejemplar.idSocio}")
            .setNegativeButton("Cancelar",null)
            .setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialogInterface, i ->
                mViewmodel.devolver(ejemplar)
                Snackbar.make(requireView(),"Ejemplar devuelto", Snackbar.LENGTH_SHORT).show()
            })
            .show()
    }
}

class DevolverViewModelFactory(private val db: VideoClubDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DevolverViewModel::class.java)) {
            return DevolverViewModel(db) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }
}
