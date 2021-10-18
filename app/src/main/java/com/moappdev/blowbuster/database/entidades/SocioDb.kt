package com.moappdev.blowbuster.database.entidades

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "socio")
class SocioDb(
    @PrimaryKey(autoGenerate = true)
    val idSocio: Long=0L,
    var nombre:String="",
    var apellido:String="",
    var fotoPerfil:String="",
    var telefono:String=""
):Parcelable