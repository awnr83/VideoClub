package com.moappdev.blowbuster.socios.detalleSocio

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetalleSocioViewModel(db:VideoClubDatabase,val mSocio: SocioDb):ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryVideoClub(db)

    var nombre=mSocio.nombre
    var apellido=mSocio.apellido
    var telefono=mSocio.telefono
    var img= mSocio.fotoPerfil?:""

    private val _datos= MutableLiveData<Boolean>()
    val datos: LiveData<Boolean>
        get()=_datos
    private val _guardado= MutableLiveData<Boolean>()
    val guardado: LiveData<Boolean>
        get()=_guardado
    private val _cancelar= MutableLiveData<Boolean>()
    val cancelar: LiveData<Boolean>
        get()=_cancelar

    init {
        _datos.value=false
        _guardado.value=false
        _cancelar.value=false
    }

    fun agregar(){
        if(apellido!="" && nombre !="" && mPortadaUri.toString()!="") {
            mSocio.nombre=nombre
            mSocio.apellido=apellido
            mSocio.telefono=telefono
            if(mPortadaUri.toString()!=null)
                mSocio.fotoPerfil= mPortadaUri.toString()
            save()
        }else
            _datos.value=true
    }

    private fun save() {
        viewModelScope.launch{

            if(mSocio.idSocio>0)
                repository.actualizarSocio(mSocio)
            else
                repository.agregarSocio(mSocio)
        }
        _guardado.value= true
    }
    fun cancelar(){
        _cancelar.value=true
    }
}