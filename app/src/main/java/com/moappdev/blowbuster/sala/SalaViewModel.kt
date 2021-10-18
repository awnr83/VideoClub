package com.moappdev.blowbuster.sala

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.*

class SalaViewModel(val db:VideoClubDatabase): ViewModel() {
    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryVideoClub(db)
    val vhs= repository.vhs

    fun estadoVhs(ejemplar: EjemplarDb){
        viewModelScope.launch{
            Log.i("alfredo","1: ${ejemplar.vhsSala}")
            ejemplar.vhsSala = !ejemplar.vhsSala
            Log.i("alfredo","2: ${ejemplar.vhsSala}")
            repository.actualizarEjemplar(ejemplar)
        }
    }
}