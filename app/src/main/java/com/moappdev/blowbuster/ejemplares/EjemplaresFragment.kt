package com.moappdev.blowbuster.ejemplares

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.databinding.FragmentEjemplaresBinding
import com.moappdev.blowbuster.ejemplares.detalleEjemplar.DetalleEjemplarFragmentArgs

class EjemplaresFragment : Fragment() {

    private lateinit var mBinding: FragmentEjemplaresBinding
    private lateinit var mViewModel: EjemplaresViewModel
    private lateinit var mViewModelFactory: EjemplaresViewModelFactory
    private lateinit var mAdapter: EjemplaresAdaptar
    private enum class INTENT(val opc: Int){ MODIFICAR(0), ALQUILAR(1)}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentEjemplaresBinding.inflate(inflater)

        val application= requireNotNull(this.activity).application
        val db= VideoClubDatabase.getInstance(application)
        mViewModelFactory= EjemplaresViewModelFactory(db)
        mViewModel= ViewModelProvider(this,mViewModelFactory).get(EjemplaresViewModel::class.java)
        mBinding.viewmodel=mViewModel
        mBinding.lifecycleOwner=this

        mAdapter= EjemplaresAdaptar(EjemplaresAdaptar.ClickListener{
            mostarDialog(it)
        },EjemplaresAdaptar.ClickLongListener{
            Snackbar.make(mBinding.root,"Ejemplar ${it.nombre} eliminado ",Snackbar.LENGTH_SHORT).show()
            mViewModel.eliminar(it)
        })
        mBinding.rvEjemplares.adapter=mAdapter
        mViewModel.allEjemplares.observe(viewLifecycleOwner, Observer {
            it?.let{
                mAdapter.submitList(it)
            }
        })

        mViewModel.agregarE.observe(viewLifecycleOwner, Observer {
            if(it){
                //AddDialogFragment().show(parentFragmentManager,AddDialogFragment::class.java.simpleName)
               findNavController().navigate(EjemplaresFragmentDirections.actionEjemplaresFragmentToDetalleEjemplarFragment(EjemplarDb()))
            }
        })

        return mBinding.root
    }

    private fun mostarDialog(ejemplar: EjemplarDb) {
        var opciones=resources.getStringArray(R.array.dialog_opciones)
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.dialog_title)
            .setIcon(R.drawable.ic_photo)
            .setItems(opciones, DialogInterface.OnClickListener { dialog, which ->
                when(which) {
                    INTENT.MODIFICAR.opc->findNavController().navigate(EjemplaresFragmentDirections.actionEjemplaresFragmentToPrestarFragment())
                    INTENT.ALQUILAR.opc-> findNavController().navigate(EjemplaresFragmentDirections.actionEjemplaresFragmentToDetalleEjemplarFragment(ejemplar))
                }
            })
            .show()
    }
}