package com.moappdev.blowbuster.socios.detalleSocio

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
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.databinding.FragmentDetalleSocioBinding
import com.moappdev.blowbuster.databinding.FragmentSociosBinding

var mPortadaUri: Uri?=null

class DetalleSocioFragment : Fragment() {

    private lateinit var mBinding: FragmentDetalleSocioBinding
    private lateinit var mViewModel: DetalleSocioViewModel
    private lateinit var mViewModelFactory: DetalleSocioViewModelFactory

    private val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            mPortadaUri = it.data?.data
            Glide.with(this)
                .load(mPortadaUri)
                .apply(
                    RequestOptions()
                        .error(R.drawable.ic_broken)
                        .placeholder(R.drawable.load_animation))
                .centerCrop()
                .into(mBinding.imgPerfil)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentDetalleSocioBinding.inflate(inflater)

        val socio= DetalleSocioFragmentArgs.fromBundle(arguments!!).socio
        if(socio.idSocio>0)
            mBinding.btnAgregar.text="Modificar"

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        mViewModelFactory= DetalleSocioViewModelFactory(db,socio)
        mViewModel= ViewModelProvider(this,mViewModelFactory).get(DetalleSocioViewModel::class.java)
        mBinding.viewmodel=mViewModel
        mBinding.lifecycleOwner=this


        mBinding.btnImg.setOnClickListener {
            val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            resultLauncher.launch(intent)
        }

        mViewModel.datos.observe(viewLifecycleOwner, Observer {
            if(it){
                validarDatos(mBinding.tilApellido)
                validarDatos(mBinding.tilNombre)
                Toast.makeText(context,getString(R.string.fde_faltan_datos), Toast.LENGTH_SHORT).show()

            }
        })
        mViewModel.guardado.observe(viewLifecycleOwner, Observer {
            if(it){
                if(socio.idSocio>0)
                    Snackbar.make(mBinding.root,"Socio modificado",Snackbar.LENGTH_SHORT).show()
                else
                    Snackbar.make(mBinding.root,"Socio guardado",Snackbar.LENGTH_SHORT).show()

                findNavController().navigate(DetalleSocioFragmentDirections.actionDetalleSocioFragmentToSociosFragment())
            }
        })
        mViewModel.cancelar.observe(viewLifecycleOwner, Observer {
            if(it)
                findNavController().navigate(DetalleSocioFragmentDirections.actionDetalleSocioFragmentToSociosFragment())
        })

        mBinding.etApellido.addTextChangedListener { validarDatos(mBinding.tilApellido) }
        mBinding.etNombre.addTextChangedListener { validarDatos(mBinding.tilNombre) }

        return mBinding.root
    }

    private fun validarDatos(til: TextInputLayout) {
        if (til.editText?.text.isNullOrEmpty()) {
            til.error = getString(R.string.fds_helper)
            til.editText?.requestFocus()
        } else
            til.isErrorEnabled = !til.isErrorEnabled
    }
}

class DetalleSocioViewModelFactory(private val db:VideoClubDatabase,private val socio:SocioDb):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(DetalleSocioViewModel::class.java)){
            return DetalleSocioViewModel(db,socio) as T
        }
        throw IllegalAccessException("No se pudo crear el ViewModel")
    }

}
