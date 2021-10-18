package com.moappdev.blowbuster.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding= FragmentHomeBinding.inflate(inflater)

        //Snackbar.make(mBinding.root,getString(R.string.fh_saludo), Snackbar.LENGTH_SHORT).show()

        mBinding.btnEjemplares.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEjemplaresFragment())
        }
        mBinding.btnSocios.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSociosFragment())
        }

        return mBinding.root
    }
}