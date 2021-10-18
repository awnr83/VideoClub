package com.moappdev.blowbuster.socios

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.databinding.FragmentSociosBinding
import com.moappdev.blowbuster.ejemplares.EjemplaresViewModel
import java.lang.IllegalArgumentException

class SociosFragment : Fragment() {

    private lateinit var mBinding: FragmentSociosBinding
    private lateinit var mViewModel: SocioViewModel
    private lateinit var mViewModelFactory: SocioViewModelFactory
    private lateinit var mAdapter: SocioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentSociosBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        mViewModelFactory= SocioViewModelFactory(db)
        mViewModel= ViewModelProvider(this, mViewModelFactory).get(SocioViewModel::class.java)

        mBinding.viewmodel=mViewModel
        mBinding.lifecycleOwner=this

        mAdapter= SocioAdapter(SocioAdapter.ClickPhone {
            llamar(it.telefono)
        }, SocioAdapter.ClickDelete {
            mostarDialogo(it)
        }, SocioAdapter.ClickSocio {
            findNavController().navigate(SociosFragmentDirections.actionSociosFragmentToDetalleSocioFragment(it))
        })
        mBinding.rvSocios.adapter=mAdapter
        mViewModel.allSocios.observe(viewLifecycleOwner, Observer {
            it?.let{
                mAdapter.submitList(it)
            }
        })

        mBinding.btnAgregar.setOnClickListener {
            findNavController().navigate(SociosFragmentDirections.actionSociosFragmentToDetalleSocioFragment(SocioDb()))
        }

        return mBinding.root
    }

    private fun mostarDialogo(socio: SocioDb) {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Atencion: Desea eliminar el Socio?")
            .setMessage("Esta acción pone como disponibles a todos los ejemplares que tenga el Socio.")
            .setNegativeButton("Cancelar",null)
            .setPositiveButton("Eliminar", DialogInterface.OnClickListener { dialogInterface, i ->
                mViewModel.eliminarSocio(socio)
                Snackbar.make(requireView(),"Socio eliminado", Snackbar.LENGTH_SHORT).show()
            })
            .show()
    }

    private fun llamar(telefono:String) {
        if(telefono.isNullOrEmpty()){
           Toast.makeText(context,"El socio NO tiene cargado un telefono",Toast.LENGTH_SHORT).show()
        }else{
            val i= Intent()
            i.action= Intent.ACTION_DIAL
            i.data= Uri.parse("tel: $telefono")

            if(i.resolveActivity(requireActivity().packageManager)!=null)
                startActivity(i)
            else
                Toast.makeText(requireContext(), "No se puede realizar la llamada", Toast.LENGTH_SHORT).show()
        }
    }
}

class SocioViewModelFactory(private val db: VideoClubDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SocioViewModel::class.java)) {
            return SocioViewModel(db) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }
}
