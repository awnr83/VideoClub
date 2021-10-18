package com.moappdev.blowbuster.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.moappdev.blowbuster.R
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.databinding.FragmentDialogAddBinding

class AddDialogFragment:DialogFragment(), DialogInterface.OnShowListener {
    private lateinit var mBinding: FragmentDialogAddBinding

    private lateinit var okButton: Button
    private lateinit var cancelarButton: Button

    private var mEjemplar: EjemplarDb?=null
    private var mPortadaUri: Uri?=null

    private val resultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK){
            mPortadaUri= it.data?.data

            // mBinding?.imgProducto.setImageURI(mPhotoSelectedUri)     //Se puede cargar assi o por Glide
            Glide.with(this)
                .load(mPortadaUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL) //guarda toda la imagen
                .centerCrop()
                .into(mBinding.imgPortada)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let { activity->
            mBinding= FragmentDialogAddBinding.inflate(LayoutInflater.from(context))
            mBinding?.let {binding->
                val builder= AlertDialog.Builder(activity)
                    .setTitle(getString(R.string.da_title))
                    .setPositiveButton(getString(R.string.da_agregar),null)
                    .setNegativeButton(getString(R.string.da_cancelar),null)
                    .setView(binding.root)

                val dialog= builder.create()
                dialog.setOnShowListener(this)

                binding.rbDvd.setOnClickListener {
                    binding.tilDvdZona.visibility= View.VISIBLE
                    binding.tilVhsFabricacion.visibility= View.GONE
                }
                binding.rbVhs.setOnClickListener {
                    binding.tilVhsFabricacion.visibility= View.VISIBLE
                    binding.tilDvdZona.visibility= View.GONE
                }
                binding.rbBlue.setOnClickListener {
                    binding.tilVhsFabricacion.visibility= View.GONE
                    binding.tilDvdZona.visibility= View.GONE
                }

                return dialog
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onShow(p0: DialogInterface?) {
        configBuscarImagen()

        val dialog= dialog as? AlertDialog
        dialog?.let {
            okButton=it.getButton(Dialog.BUTTON_POSITIVE)
            cancelarButton=it.getButton(Dialog.BUTTON_NEGATIVE)

            okButton.setOnClickListener {
                mBinding?.let {
                    //val ejemplar=EjemplarDb(it.)

                    // var nombre: String="",
                    //     var publicacion: Int=0,
                    //     val Idiomas: String="",
                    //     var tipo:String="",          //dvd, vhs, blue
                    //     var zona:Int?=null,          //dvd
                    //     var fabricacion:Int?=null    //vhs
                }
            }
            cancelarButton.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun configBuscarImagen() {
        mBinding?.let {
            it.btnBuscarImg.setOnClickListener {
                //abrir galeria
                val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                resultLauncher.launch(intent)
            }
        }
    }
}