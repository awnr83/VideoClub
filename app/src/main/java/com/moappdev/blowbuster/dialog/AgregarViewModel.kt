package com.moappdev.blowbuster.dialog

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moappdev.blowbuster.Constant
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.dao.EjemplaresDatabaseDao
import kotlinx.coroutines.*

class AgregarViewModel(private val db: EjemplaresDatabaseDao):ViewModel() {
    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    var titulo=""
    var publicacion=""
    var img="a"
    var zonaDvd=""
    var fabVhs=""
/*    private val _titulo= MutableLiveData<String>()
    val titulo: LiveData<String>
        get()=_titulo
    private val _publicacion= MutableLiveData<String>()
    val publicacion: LiveData<String>
        get()=_publicacion
    private val _img= MutableLiveData<String>()
    val img: LiveData<String>
        get()=_img
    private val _zonaDvd= MutableLiveData<Int>()
    val zonaDvd: LiveData<Int>
        get()=_zonaDvd
    private val _fabVhs= MutableLiveData<Int>()
    val fabVhs: LiveData<Int>
        get()=_fabVhs

 */

    private val _vhs= MutableLiveData<Int>()
    val vhs: LiveData<Int>
        get()=_vhs
    private val _dvd= MutableLiveData<Int>()
    val dvd: LiveData<Int>
        get()=_dvd
    private val _blue= MutableLiveData<Int>()
    val blue: LiveData<Int>
        get()=_blue
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
        _dvd.value=View.VISIBLE
        _vhs.value=View.GONE
        _blue.value=View.GONE
        _datos.value=false
        _guardado.value=false
        _cancelar.value=false
    }

    fun onVhs(){
        _dvd.value=View.GONE
        _vhs.value=View.VISIBLE
        _blue.value=View.GONE
    }
    fun onDvd(){
        _dvd.value=View.VISIBLE
        _vhs.value=View.GONE
        _blue.value=View.GONE
    }
    fun onBlue(){
        _dvd.value=View.GONE
        _vhs.value=View.GONE
        _blue.value=View.VISIBLE
    }
    fun onCancelar(){
        _cancelar.value=true
    }

    fun agregar(){
        if(titulo!="" && publicacion !="" && img!="") {
            var ejemplar = EjemplarDb()
            ejemplar.nombre=titulo
            ejemplar.img= mPortadaUri.toString()
            ejemplar.publicacion=publicacion.toInt()

            if (_blue.value == View.VISIBLE) {
                ejemplar.tipo = Constant.BLUERAY
                save(ejemplar)
            } else if (_vhs.value == View.VISIBLE) {
                ejemplar.tipo = Constant.VHS
                if(fabVhs=="")
                    _datos.value=true
                else{
                    ejemplar.zona=fabVhs.toInt()
                    save(ejemplar)
                }
            } else if (_dvd.value == View.VISIBLE) {
                ejemplar.tipo = Constant.DVD
                if(zonaDvd=="")
                    _datos.value=true
                else{
                    Log.i("alfredo","llego hasta aca")
                    ejemplar.zona=zonaDvd.toInt()
                    save(ejemplar)
                }
            }
        }else
            _datos.value=true
    }

    private fun save(ejemplar: EjemplarDb) {
        uiScope.launch{
            withContext(Dispatchers.IO){
                db.insert(ejemplar)
            }
        }
        _guardado.value= true
    }
}