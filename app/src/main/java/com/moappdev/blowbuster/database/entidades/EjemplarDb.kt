package com.moappdev.blowbuster.database.entidades

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ejemplar")
class EjemplarDb(
     @PrimaryKey(autoGenerate = true)
         val codigo: Long=0L,
     var nombre: String="",
     var publicacion: Int=0,
     var img: String="",
     val idiomas: String="",
     var tipo:String="",          //dvd, vhs, blue
     var zona:Int?=null,          //dvd
     var fabricacion:Int?=null,    //vhs
     var idSocio:Long?=null,
     var prestado:Boolean=false,
     var vhsSala:Boolean=false
):Parcelable