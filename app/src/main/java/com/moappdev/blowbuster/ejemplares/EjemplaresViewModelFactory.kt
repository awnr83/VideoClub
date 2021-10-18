package com.moappdev.blowbuster.ejemplares

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moappdev.blowbuster.database.VideoClubDatabase
import java.lang.IllegalArgumentException

class EjemplaresViewModelFactory(private val database:VideoClubDatabase):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EjemplaresViewModel::class.java)) {
            return EjemplaresViewModel(database) as T
        }
        throw IllegalArgumentException("no se pudo crear el ViewModel")
    }
}