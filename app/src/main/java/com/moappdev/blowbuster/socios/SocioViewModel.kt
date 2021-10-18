package com.moappdev.blowbuster.socios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SocioViewModel(private val db: VideoClubDatabase):ViewModel() {
    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private var repository= RepositoryVideoClub(db)
    var allSocios= repository.socios

    fun eliminarSocio(socio: SocioDb){
        viewModelScope.launch {
            repository.eliminarSocio(socio)
        }
    }

}