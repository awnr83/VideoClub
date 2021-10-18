package com.moappdev.blowbuster.prestar

import android.util.Log
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
    private val _vSocio= MutableLiveData<Int>()
    val vSocio: LiveData<Int>
        get()=_vSocio

    private val _ejemplar= MutableLiveData<EjemplarDb>()
    val ejemplar: LiveData<EjemplarDb>
        get()=_ejemplar
    private val _noEjemplar= MutableLiveData<String>()
    val noEjemplar: LiveData<String>
        get()=_noEjemplar
    private val _vEjemplar= MutableLiveData<Int>()
    val vEjemplar: LiveData<Int>
        get()=_vEjemplar

    private val _prestado= MutableLiveData<Boolean>()
    val prestado: LiveData<Boolean>
        get()=_prestado
    private val _datos= MutableLiveData<Boolean>()
    val datos: LiveData<Boolean>
        get()=_datos

    private val _okPrestado= MutableLiveData<Boolean>()
    val okPrestado: LiveData<Boolean>
        get()=_okPrestado

    private val _vTextSocio= MutableLiveData<Int>()
    val vTextSocio: LiveData<Int>
        get()=_vTextSocio
    private val _vTextEjemplar= MutableLiveData<Int>()
    val vTextEjemplar: LiveData<Int>
        get()=_vTextEjemplar

    init {
        nroSocio.value=""
        nroEjemplar.value=""

        _datos.value=false

        _ejemplar.value = EjemplarDb()
        _socio.value = SocioDb()

        _vTextSocio.value=View.INVISIBLE

        _vEjemplar.value= View.INVISIBLE
        _vSocio.value= View.INVISIBLE

        _prestado.value=false   //esta prestado
        _okPrestado.value=false //ok se pudo prestar
    }

    fun buscarSocio(){
        viewModelScope.launch {
            if(nroSocio.value!=null && nroSocio.value!="") {
                val socio = repository.buscarSocio(nroSocio.value.toString())
                if (socio != null){
                    _vSocio.value= View.VISIBLE
                    _vTextSocio.value= View.INVISIBLE
                    _socio.value = socio
                }else{
                    _vSocio.value= View.INVISIBLE
                    _vTextSocio.value= View.VISIBLE
                    _socio.value = SocioDb()
                }
            }else
                _vTextSocio.value= View.VISIBLE
        }
    }
    fun buscarEjemplar(){
        viewModelScope.launch {
            if(nroEjemplar.value!=null && nroEjemplar.value!="") {
                val ejemplar= repository.buscarEjemplar(nroEjemplar.value.toString())
                if(ejemplar!=null) {
                    if (ejemplar.tipo!=Constant.VHS && !ejemplar.prestado) {
                        _vEjemplar.value = View.VISIBLE
                        _vTextEjemplar.value= View.INVISIBLE
                        _ejemplar.value = ejemplar
                    }else {
                        if(ejemplar.tipo==Constant.VHS)
                            _noEjemplar.value="El ejemplar NO se puede usar porque es un VHS"
                        else
                            _prestado.value=ejemplar.prestado

                        _vEjemplar.value=View.INVISIBLE
                        _vTextEjemplar.value= View.VISIBLE
                        _ejemplar.value = EjemplarDb()
                    }
                }else{
                    _noEjemplar.value= "No existe el ejemplar"
                    _vEjemplar.value=View.INVISIBLE
                    _vTextEjemplar.value= View.VISIBLE
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

                _vSocio.value=View.INVISIBLE
                _vEjemplar.value=View.INVISIBLE

                nroEjemplar.value=""
                nroSocio.value=""

                _ejemplar.value= EjemplarDb()
                _socio.value=SocioDb()
            }
        }else
            _datos.value=true
    }
}