package com.moappdev.blowbuster.prestar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.databinding.FragmentPrestarBinding
import com.moappdev.blowbuster.devolver.DevolverAdapter
import java.lang.IllegalArgumentException

class PrestarFragment : Fragment() {

    private lateinit var mBinding: FragmentPrestarBinding
    private lateinit var mViewModel: PrestarViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentPrestarBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        val mViewModelFactory= PrestarViewModelFactory(db)
        mViewModel= ViewModelProvider(this, mViewModelFactory).get(PrestarViewModel::class.java)
        mBinding.viewModel= mViewModel
        mBinding.lifecycleOwner=this

        mViewModel.nroSocio.observe(viewLifecycleOwner, Observer {
            if(it!=null && it!=""){
                mBinding.tilSocio.isErrorEnabled=false
                mViewModel.buscarSocio()
            }else
                mBinding.tilSocio.isErrorEnabled=true
        })

        mViewModel.nroEjemplar.observe(viewLifecycleOwner, Observer {
            if(it!=null && it!=""){
                mBinding.tilEjemplar.isErrorEnabled=false
                mViewModel.buscarEjemplar()
            }else
                mBinding.tilEjemplar.isErrorEnabled=true

        })

        mViewModel.okPrestado.observe(viewLifecycleOwner, Observer {
            if(it){
                Snackbar.make(mBinding.root, "El ejemplar se presto", Snackbar.LENGTH_SHORT).show()
            }
        })
        mViewModel.datos.observe(viewLifecycleOwner, Observer {
            if(it){
                validarDatos(mBinding.tilSocio)
                validarDatos(mBinding.tilEjemplar)
                Toast.makeText(context,getString(R.string.fde_faltan_datos), Toast.LENGTH_SHORT).show()
            }
        })
        mBinding.etEjemplar.addTextChangedListener{validarDatos(mBinding.tilEjemplar)}
        mBinding.etSocio.addTextChangedListener{validarDatos(mBinding.tilSocio)}

        return mBinding.root
    }

    private fun validarDatos(til: TextInputLayout) {
        if (til.editText?.text.isNullOrEmpty()) {
            til.error = getString(R.string.da_helper)
            til.editText?.requestFocus()
        } else
            til.isErrorEnabled = !til.isErrorEnabled
    }
}

class PrestarViewModelFactory(private val db:VideoClubDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PrestarViewModel::class.java)) {
            return PrestarViewModel(db) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }

}
