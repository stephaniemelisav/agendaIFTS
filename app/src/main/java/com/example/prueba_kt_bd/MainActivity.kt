package com.example.prueba_kt_bd

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prueba_kt_bd.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuscar.setOnClickListener{

            val searchMateria: String = binding.etSearchMateria.text.toString()
            //Si no esta vacio, entonces suelto la funcion de leer datos
            if (searchMateria.isNotEmpty()){
                readData(searchMateria)
            }else{
                //si no, salta mensaje de que complete ese espacio
                Toast.makeText(this,"Por favor ingrese una materia valida",Toast.LENGTH_LONG)
            }

        }
    }

    //
    private fun readData(materia: String){
        //referencia a la bd que tenemos en fire con name evento
        databaseReference = FirebaseDatabase.getInstance().getReference("evento")
        databaseReference.child(materia).get().addOnSuccessListener {
            //si existe, lo extrae
            if (it.exists()){
                val fecha = it.child("fecha").value
                val hora = it.child("hora").value
                val nombreExamen = it.child("nombreExamen").value
                val tipo = it.child("tipo").value

                Toast.makeText(this, "Resultados", Toast.LENGTH_LONG).show()

                binding.etSearchMateria.text.clear()
                //datos obtenidos convertidos a cadenas
                binding.tvFechaEvento.text = fecha.toString()
                binding.tvHoraEvento.text = hora.toString()
                binding.tvDescriEvento.text = nombreExamen.toString()
                binding.tvTipoEvento.text = tipo.toString()

            }else{
                Toast.makeText(this,"La materia no existe.", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { e ->
            // Mensaje en caso de falla
            Toast.makeText(this, "Operación fallida: ${e.message}", Toast.LENGTH_LONG).show()
            Log.e("UploadActivity", "Error al guardar el evento", e)
        }

    }

}

