package com.moappdev.blowbuster.ejemplares.detalleEjemplar

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.FragmentDetalleEjemplarBinding
import java.lang.IllegalArgumentException

var mPortadaUriEjemplar: Uri?=null

class DetalleEjemplarFragment : Fragment() {

    private lateinit var mBinding: FragmentDetalleEjemplarBinding
    private lateinit var mViewModel: DetalleEjemplarViewModel
    private lateinit var mViewModelFactory: DetalleEjemplarViewModelFactory
    private lateinit var ejemplar:EjemplarDb

    private val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
//            if(!ejemplar.img.isNullOrEmpty())
//                mPortadaUriEjemplar = Uri.parse(ejemplar.img)
//            else
                mPortadaUriEjemplar = it.data?.data
            Glide.with(this)
                .load(mPortadaUriEjemplar)
                .apply(
                    RequestOptions()
                    .error(R.drawable.ic_broken)
                    .placeholder(R.drawable.load_animation))
                .centerCrop()
                .into(mBinding.imgPortada)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentDetalleEjemplarBinding.inflate(inflater)

        ejemplar= DetalleEjemplarFragmentArgs.fromBundle(arguments!!).ejemplar
        if(ejemplar.codigo>0)
            mBinding.btnAgregar.text="Modificar"

        val application= requireNotNull(this.activity).application
        val database= VideoClubDatabase.getInstance(application)
        mViewModelFactory= DetalleEjemplarViewModelFactory(database,ejemplar)
        mViewModel= ViewModelProvider(this,mViewModelFactory).get(DetalleEjemplarViewModel::class.java)
        mBinding.viewmodel= mViewModel
        mBinding.lifecycleOwner=this


        mViewModel.datos.observe(viewLifecycleOwner, Observer {
            if(it){
                validarDatos(mBinding.tilTitulo)
                validarDatos(mBinding.tilPublicacion)
                validarDatos(mBinding.tilDvdZona)
                validarDatos(mBinding.tilVhsFabricacion)
                Toast.makeText(context,getString(R.string.fde_faltan_datos), Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.guardado.observe(viewLifecycleOwner, Observer {
            if(it){
                Snackbar.make(mBinding.root,getString(R.string.fde_msg_guardado), Snackbar.LENGTH_SHORT).show()
                findNavController().navigate(DetalleEjemplarFragmentDirections.actionDetalleEjemplarFragmentToEjemplaresFragment())
            }
        })
        mViewModel.cancelar.observe(viewLifecycleOwner, Observer {
            if(it){
                findNavController().navigate(DetalleEjemplarFragmentDirections.actionDetalleEjemplarFragmentToEjemplaresFragment())
            }
        })

        mBinding.btnBuscarImg.setOnClickListener {
            val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }
        mBinding.etTitulo.addTextChangedListener { validarDatos(mBinding.tilTitulo) }
        mBinding.etPublicacion.addTextChangedListener { validarDatos(mBinding.tilPublicacion) }
        mBinding.etDvdZona.addTextChangedListener { validarDatos(mBinding.tilDvdZona) }
        mBinding.etVhsFabricacion.addTextChangedListener { validarDatos(mBinding.tilVhsFabricacion) }

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

class DetalleEjemplarViewModelFactory(private val db: VideoClubDatabase, private var ejemplar: EjemplarDb):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetalleEjemplarViewModel::class.java)) {
            return DetalleEjemplarViewModel(db,ejemplar) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }
}
