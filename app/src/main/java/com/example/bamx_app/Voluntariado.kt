package com.example.bamx_app

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.*

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Voluntariado.newInstance] factory method to
 * create an instance of this fragment.
 */
class Voluntariado : Fragment(), View.OnClickListener {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var database : DatabaseReference
    private lateinit var db : DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db = DBHelper(activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_voluntariado, container, false)
        val botton: Button = view.findViewById(R.id.botonEnviar)
        botton.setOnClickListener(this)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.botonEnviar -> {
                val nombre: TextInputEditText = requireView().findViewById(R.id.nombre)
                val empresa: TextInputEditText = requireView().findViewById(R.id.empresa)
                val email: TextInputEditText = requireView().findViewById(R.id.email)
                val telefono: TextInputEditText = requireView().findViewById(R.id.telefono)
                val mensaje: TextInputEditText = requireView().findViewById(R.id.mensaje)

                database = FirebaseDatabase.getInstance().getReference("Voluntarios")
                val voluntario = voluntario(LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss")), nombre.text.toString(), empresa.text.toString(), email.text.toString(), telefono.text.toString(), mensaje.text.toString())
                if (voluntario.nombre!!.isNotEmpty() && voluntario.empresa!!.isNotEmpty() && voluntario.email!!.isNotEmpty() && voluntario.telefono!!.isNotEmpty() && voluntario.mensaje!!.isNotEmpty()){
                    database.child(nombre.text.toString()).setValue(voluntario).addOnSuccessListener {
                        db.voluntariado()
                        nombre.text!!.clear()
                        empresa.text!!.clear()
                        email.text!!.clear()
                        telefono.text!!.clear()
                        mensaje.text!!.clear()
                        Toast.makeText(
                            activity?.applicationContext,
                            "Registro exitoso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener{
                        Toast.makeText(activity?.applicationContext, "Error en el registro",Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity?.applicationContext, "Todos los campos son mandatorios",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Voluntariado.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Voluntariado().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}