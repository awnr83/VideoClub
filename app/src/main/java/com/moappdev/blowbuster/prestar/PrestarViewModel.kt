package com.moappdev.blowbuster.prestar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moappdev.blowbuster.Constant
import com.moappdev.blowbuster.database.entidades.SocioDb
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PrestarViewModel(db:VideoClubDatabase):ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryVideoClub(db)

    val nroSocio= MutableLiveData<String>()
    val nroEjemplar= MutableLiveData<String>()

    private val _socio= MutableLiveData<SocioDb>()
    val socio: LiveData<SocioDb>
        get()=_socio
    private val _noSocio= MutableLiveData<Boolean>()
    val noSocio: LiveData<Boolean>
        get()=_noSocio
    private val _vSocio= MutableLiveData<Int>()
    val vSocio: LiveData<Int>
        get()=_vSocio

    private val _ejemplar= MutableLiveData<EjemplarDb>()
    val ejemplar: LiveData<EjemplarDb>
        get()=_ejemplar
    private val _noEjemplar= MutableLiveData<Boolean>()
    val noEjemplar: LiveData<Boolean>
        get()=_noEjemplar
    private val _vEjemplar= MutableLiveData<Int>()
    val vEjemplar: LiveData<Int>
        get()=_vEjemplar

    private val _tipo= MutableLiveData<Boolean>()
    val tipo: LiveData<Boolean>
        get()=_tipo
    private val _prestado= MutableLiveData<Boolean>()
    val prestado: LiveData<Boolean>
        get()=_prestado

    private val _okPrestado= MutableLiveData<Boolean>()
    val okPrestado: LiveData<Boolean>
        get()=_okPrestado

    init {
        nroSocio.value=""
        nroEjemplar.value=""

        _ejemplar.value = EjemplarDb()
        _socio.value = SocioDb()

        _vEjemplar.value= View.INVISIBLE
        _vSocio.value= View.INVISIBLE

        _noSocio.value=false
        _noEjemplar.value=false
        _tipo.value=false       //es vhs
        _prestado.value=false   //esta prestado
        _okPrestado.value=false //ok se pudo prestar
    }

    fun buscarSocio(){
        viewModelScope.launch {
            if(nroSocio.value!=null && nroSocio.value!="") {
                val socio = repository.buscarSocio(nroSocio.value.toString())
                if (socio != null){
                    _noSocio.value = false
                    _vSocio.value= View.VISIBLE
                    _socio.value = socio
                }else{
                    _noSocio.value=true
                    _vSocio.value= View.INVISIBLE
                    _socio.value = SocioDb()
                }
            }
        }
    }
    fun buscarEjemplar(){
        viewModelScope.launch {
            if(nroEjemplar.value!=null && nroEjemplar.value!="") {
                val ejemplar= repository.buscarEjemplar(nroEjemplar.value.toString())
                if(ejemplar!=null) {
                    if (ejemplar.tipo!=Constant.VHS && !ejemplar.prestado) {
                        _noEjemplar.value = false
                        _tipo.value = false
                        _vEjemplar.value = View.VISIBLE
                        _ejemplar.value = ejemplar
                    }else {
                        if(ejemplar.tipo==Constant.VHS)
                            _tipo.value = true
                        else
                            _prestado.value=ejemplar.prestado

                        _vEjemplar.value=View.INVISIBLE
                        _ejemplar.value = EjemplarDb()
                    }
                }else{
                    _noEjemplar.value= true
                    _tipo.value = false
                    _vEjemplar.value=View.INVISIBLE
                    _ejemplar.value = EjemplarDb()
                }
            }
        }
    }

    fun prestar(){
        val socio= _socio.value as SocioDb
        val ejemplar= _ejemplar.value as EjemplarDb
        if(ejemplar.tipo!=Constant.VHS && !ejemplar.prestado && socio.idSocio!=0L){
            viewModelScope.launch {
                ejemplar.prestado=true
                ejemplar.idSocio=socio.idSocio
                repository.prestarEjemplar(ejemplar)
                _okPrestado.value=true

                nroEjemplar.value=""
                nroSocio.value=""

                _ejemplar.value= EjemplarDb()
                _socio.value=SocioDb()
            }
        }
    }
}