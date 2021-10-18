package com.moappdev.blowbuster.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.moappdev.blowbuster.database.entidades.SocioDb

@Dao
interface SociosDatabaseDao {
    @Insert
    fun insertSocio(socioDb: SocioDb)

    @Query("select * from socio order by idSocio asc")
    fun allSocios():LiveData<List<SocioDb>>

    @Query("select * from socio where idSocio= :id")
    fun obtenerSocio(id:Long): SocioDb

    @Delete
    fun eliminarSocio(socio: SocioDb)

    @Update
    fun actualizarSocio(socio: SocioDb)
}