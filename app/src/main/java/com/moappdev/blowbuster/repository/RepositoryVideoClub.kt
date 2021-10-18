package com.moappdev.blowbuster.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.VideoClubDatabase
import com.moappdev.blowbuster.database.entidades.SocioDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryVideoClub(private val db: VideoClubDatabase) {

    var ejemplares: LiveData<List<EjemplarDb>> = db.ejemplaresDataBaseDao.allEjemplares()
    var socios: LiveData<List<SocioDb>> = db.sociosDataBaseDao.allSocios()

    var ejemplaresPrestados: LiveData<List<EjemplarDb>> = db.ejemplaresDataBaseDao.prestados()

    var vhs: LiveData<List<EjemplarDb>> = db.ejemplaresDataBaseDao.allVhs()

    suspend fun agregarEjemplar(ejemplar: EjemplarDb){
        withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.insert(ejemplar)
        }
    }
    suspend fun agregarSocio(socio: SocioDb){
        withContext(Dispatchers.IO){
            db.sociosDataBaseDao.insertSocio(socio)
        }
    }

    suspend fun eliminarEjemplar(ejemplar: EjemplarDb){
        withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.eliminar(ejemplar)
        }
    }

    suspend fun eliminarSocio(socio: SocioDb){
        withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.devolverEjemplarSocioEliminado(socio.idSocio)
            db.sociosDataBaseDao.eliminarSocio(socio)
        }
    }


    suspend fun buscarSocio(id: String): SocioDb {
        return withContext(Dispatchers.IO){
            db.sociosDataBaseDao.obtenerSocio(id.toLong())
        }
    }
    suspend fun buscarEjemplar(imdb: String): EjemplarDb? {
        return withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.obtenerEjemplar(imdb.toLong())
        }
    }

    suspend fun prestarEjemplar(ejemplar: EjemplarDb){
        withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.prestarEjemplar(ejemplar)
        }
    }
    suspend fun devolverEjemplar(ejemplar: EjemplarDb){
        withContext(Dispatchers.IO){
            ejemplar.prestado=false
            ejemplar.idSocio=null
            db.ejemplaresDataBaseDao.actualizarEjemplar(ejemplar)
        }
    }
    suspend fun actualizarEjemplar(ejemplar: EjemplarDb){
        withContext(Dispatchers.IO){
            db.ejemplaresDataBaseDao.actualizarEjemplar(ejemplar)
        }
    }
    suspend fun actualizarSocio(socio:SocioDb){
        withContext(Dispatchers.IO){
            db.sociosDataBaseDao.actualizarSocio(socio)
        }
    }

}