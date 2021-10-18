package com.moappdev.blowbuster.ejemplares.detalleEjemplar


import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moappdev.blowbuster.Constant
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.repository.RepositoryVideoClub
import kotlinx.coroutines.*

class DetalleEjemplarViewModel(private val db: VideoClubDatabase, private var mEjemplar: EjemplarDb):ViewModel() {

    private val jobViewModel= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + jobViewModel)
    override fun onCleared() {
        super.onCleared()
        jobViewModel.cancel()
    }

    private val repository= RepositoryVideoClub(db)

    var titulo=mEjemplar.nombre
    var img=mEjemplar.img
    var publicacion=valor(mEjemplar.publicacion)
    var zonaDvd= valor(mEjemplar.zona)
    var fabVhs=valor(mEjemplar.fabricacion)


    private val _vhs= MutableLiveData<Int>()
    val vhs: LiveData<Int>
        get()=_vhs
    private val _dvd= MutableLiveData<Int>()
    val dvd: LiveData<Int>
        get()=_dvd
    private val _blue= MutableLiveData<Int>()
    val blue: LiveData<Int>
        get()=_blue

    private val _cDvd= MutableLiveData<Boolean>()
    val cDvd: LiveData<Boolean>
        get()=_cDvd
    private val _cVhs= MutableLiveData<Boolean>()
    val cVhs: LiveData<Boolean>
        get()=_cVhs
    private val _cBlue= MutableLiveData<Boolean>()
    val cBlue: LiveData<Boolean>
        get()=_cBlue

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
        when(mEjemplar.tipo){
            Constant.DVD->{
                _cDvd.value= true
            }
            Constant.VHS->{
                _cVhs.value= true
            }
            Constant.BLUERAY->{
                _cBlue.value= true
            }
            else->{
                _cDvd.value= true
            }
        }
        _dvd.value= View.VISIBLE
        _vhs.value= View.GONE
        _blue.value= View.GONE
        _datos.value=false
        _guardado.value=false
        _cancelar.value=false
    }

    fun onVhs(){
        _dvd.value= View.GONE
        _vhs.value= View.VISIBLE
        _blue.value= View.GONE
    }
    fun onDvd(){
        _dvd.value= View.VISIBLE
        _vhs.value= View.GONE
        _blue.value= View.GONE
    }
    fun onBlue(){
        _dvd.value= View.GONE
        _vhs.value= View.GONE
        _blue.value= View.VISIBLE
    }

    fun agregar(){
        if(titulo!="" && publicacion !="" && mPortadaUriEjemplar.toString()!="") {
            mEjemplar.nombre=titulo
            mEjemplar.publicacion=publicacion.toInt()
            if(mPortadaUriEjemplar.toString()!=null)
                mEjemplar.img= mPortadaUriEjemplar.toString()

            if (_blue.value == View.VISIBLE) {
                mEjemplar.tipo = Constant.BLUERAY
                save()
            } else if (_vhs.value == View.VISIBLE) {
                mEjemplar.tipo = Constant.VHS
                if(fabVhs=="")
                    _datos.value=true
                else{
                    mEjemplar.zona=fabVhs.toInt()
                    save()
                }
            } else if (_dvd.value == View.VISIBLE) {
                mEjemplar.tipo = Constant.DVD
                if(zonaDvd=="")
                    _datos.value=true
                else{
                    mEjemplar.zona=zonaDvd.toInt()
                    save()
                }
            }
        }else
            _datos.value=true
    }

    fun onCancelar(){
        _cancelar.value=true
    }

    private fun save() {
        uiScope.launch{
            if(mEjemplar.codigo>0)
                repository.actualizarEjemplar(mEjemplar)
            else
                repository.agregarEjemplar(mEjemplar)
        }
        _guardado.value= true
    }

    private fun valor(valor: Int?): String {
        return if(valor==null || valor==0)
             ""
        else
            valor.toString()
    }
}