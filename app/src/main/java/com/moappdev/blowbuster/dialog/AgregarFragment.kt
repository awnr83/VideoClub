package com.moappdev.blowbuster.dialog

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.dao.EjemplaresDatabaseDao
import com.moappdev.blowbuster.databinding.FragmentAgregarBinding
import java.lang.IllegalArgumentException


var mPortadaUri: Uri?=null

class AgregarFragment : Fragment() {

    private lateinit var mBinding: FragmentAgregarBinding
    private lateinit var mViewModel: AgregarViewModel
    private lateinit var mViewModelFactory: AgregarViewModelFactory

    private val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            mPortadaUri= it.data?.data
            Glide.with(this)
                .load(mPortadaUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //guarda toda la imagen
                .apply(RequestOptions()
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
        mBinding= FragmentAgregarBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val database= VideoClubDatabase.getInstance(application).ejemplaresDataBaseDao
        mViewModelFactory= AgregarViewModelFactory(database)
        mViewModel=ViewModelProvider(this,mViewModelFactory).get(AgregarViewModel::class.java)
        mBinding.viewmodel=mViewModel
        mBinding.lifecycleOwner=this

        mViewModel.datos.observe(viewLifecycleOwner, Observer {
            if(it){
                Toast.makeText(context,getString(R.string.da_faltan_datos),Toast.LENGTH_SHORT).show()
            }
        })
        mViewModel.guardado.observe(viewLifecycleOwner, Observer {
            if(it){
                Snackbar.make(mBinding.root,"Registro guardado",Snackbar.LENGTH_SHORT).show()
                //findNavController().navigate(AgregarFragmentDirections.actionAgregarFragmentToEjemplaresFragment())
            }
        })
        mViewModel.cancelar.observe(viewLifecycleOwner, Observer {
            if(it){
              //  findNavController().navigate(AgregarFragmentDirections.actionAgregarFragmentToEjemplaresFragment())
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

class AgregarViewModelFactory(private val database: EjemplaresDatabaseDao):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AgregarViewModel::class.java)) {
            return AgregarViewModel(database) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }
}
