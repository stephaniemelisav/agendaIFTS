package com.example.crudrealtimeadmin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


//aca vamos a conectar una bd con los datos creados
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    //referencia de la bd
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload)
        //establecemos la conexión
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Probamos de usar el boton de "atras" para terminar la actividad acutual(CreateActivity) y lanzar la vista MainActivity
        binding.btnAtras.setOnClickListener {
            val intent = Intent(this@UploadActivity, MainActivity::class.java)
            startActivity(intent) // Inicia la pantalla MainActivity
            finish() // finaliza la pantalla actual (la cierra)
        }

        binding.saveButton.setOnClickListener {
            //variables que guardan los datos
            val materia = binding.etMateria.text.toString()
            val fecha = binding.etFecha.text.toString()
            val hora = binding.etHora.text.toString()
            val nombreExamen = binding.etNameExamen.text.toString()
            val tipo = binding.etTipo.text.toString()

            databaseReference = FirebaseDatabase.getInstance().getReference("evento")

            val evento = DatoFecha( materia, fecha, hora, nombreExamen, tipo)

            databaseReference.child(tipo).setValue(evento).addOnSuccessListener {
                binding.etMateria.text.clear()
                binding.etFecha.text.clear()
                binding.etHora.text.clear()
                binding.etNameExamen.text.clear()
                binding.etTipo.text.clear()

                // Muestra un pequeño aviso de que se subió correctamente
                Toast.makeText(this, "Evento guardado.", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { e ->
                // Mensaje en caso de falla
                Toast.makeText(this, "Operación fallida: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("UploadActivity", "Error al guardar el evento", e)
            }

        }
    }
}