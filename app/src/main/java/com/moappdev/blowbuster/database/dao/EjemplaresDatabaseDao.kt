package com.moappdev.blowbuster.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moappdev.blowbuster.Constant
import com.moappdev.blowbuster.database.entidades.EjemplarDb

@Dao
interface EjemplaresDatabaseDao {
    @Insert
    fun insert(ejemplar: EjemplarDb)
    @Delete
    fun eliminar(ejemplar: EjemplarDb)

    @Query("select * from ejemplar")
    fun allEjemplares():LiveData<List<EjemplarDb>>

    @Query("select * from ejemplar where codigo= :id")
    fun obtenerEjemplar(id: Long): EjemplarDb

    @Update()
    fun prestarEjemplar(ejemplar: EjemplarDb)


    @Query("select * from ejemplar where prestado=1 order by idSocio")
    fun prestados():LiveData<List<EjemplarDb>>

    @Query("update ejemplar set idSocio=null, prestado=0 where idSocio= :idSocio")
    fun devolverEjemplarSocioEliminado(idSocio:Long)

    @Update()
    fun actualizarEjemplar(ejemplar: EjemplarDb)

    @Query("select * from ejemplar where tipo= 'vhs' order by vhsSala desc")
    fun allVhs():LiveData<List<EjemplarDb>>
}