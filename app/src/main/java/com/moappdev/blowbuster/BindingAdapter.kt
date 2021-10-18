package com.moappdev.blowbuster

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("image")
fun loadImg(imgView: ImageView, imgUri:String){
    if(!imgUri.isNullOrEmpty()){
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                    .error(R.drawable.ic_broken)
                    .placeholder(R.drawable.load_animation))
            .centerCrop()
            .into(imgView)
    }
}

@BindingAdapter("imageCircle")
fun loadImgCircle(imgView: ImageView, imgUri:String){
    if(!imgUri.isNullOrEmpty()){
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .error(R.drawable.ic_broken)
                .placeholder(R.drawable.load_animation))
            .centerCrop()
            .circleCrop()
            .into(imgView)
    }
}
@BindingAdapter("textNroSocio")
fun loadImgCircle(tv: TextView, nro:Long){
    tv?.let {
        it.text="Socio nro: ${nro.toString()}"
    }
}
@BindingAdapter("textNombre")
fun textNombre(tv: TextView, nombre:String){
    tv?.let {
        it.text="Nombre: $nombre"
    }
}

@BindingAdapter("textApellido")
fun textApellido(tv: TextView, apellido:String){
    tv?.let {
        it.text="Apellido: $apellido"
    }
}
@BindingAdapter("textTelefono")
fun textTelefono(tv: TextView, telefono:String){
    tv?.let {
        it.text="Telefono: $telefono"
    }
}
@BindingAdapter("textPublicacion")
fun textPublicacion(tv: TextView, ano:Int){
    tv?.let {
        it.text="Publicacion: ${ano.toString()}"
    }
}
@BindingAdapter("textCodigo")
fun textCodigo(tv: TextView, codigo:Long){
    tv?.let {
        it.text="Codigo: ${codigo.toString()}"
    }
}