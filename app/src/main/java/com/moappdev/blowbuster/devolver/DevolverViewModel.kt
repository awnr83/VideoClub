package com.moappdev.blowbuster.devolver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DevolverViewModel(db: VideoClubDatabase):ViewModel(){
    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    val repository= RepositoryVideoClub(db)
    val prestados=repository.ejemplaresPrestados

    fun devolver(ejemplar: EjemplarDb){
        viewModelScope.launch {
            repository.devolverEjemplar(ejemplar)
        }
    }
}