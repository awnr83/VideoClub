package com.moappdev.blowbuster.ejemplares

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EjemplaresViewModel(private val db: VideoClubDatabase): ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryVideoClub(db)
    var allEjemplares= repository.ejemplares

    private val _agregarE=MutableLiveData<Boolean>()
    val agregarE: LiveData<Boolean>
        get()=_agregarE
    fun onAgregar(){
        _agregarE.value=true
    }
    fun onAgregarC(){
        _agregarE.value=false
    }

    init {
        _agregarE.value=false
    }

    fun eliminar(ejemplar:EjemplarDb){
        viewModelScope.launch {
            repository.eliminarEjemplar(ejemplar)
        }
    }

}