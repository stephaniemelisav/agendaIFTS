package com.example.crudrealtimeadmin

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


import com.example.crudrealtimeadmin.databinding.ActivityUploadBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar


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

        // Se sobreescribe la función del botón atras del sistema.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@UploadActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                }
            }
        )


        //Configurar TimePickerDialog para la hora, si clickea abre popup
        binding.etHora.setOnClickListener {
            //en val calendar guardamos una instancia de calendar, que se usa para tener la hora y minutos
            val calendar = Calendar.getInstance()
            //formato de 24hs, 0 a 23
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            //minutos
            val minute = calendar.get(Calendar.MINUTE)

            //Aca permitimos la selección de la hora al usuario (profesor)
            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                //selectedHour, selectedMinute representan los minutos y hora seleccionada
                binding.etHora.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
            }, hour, minute, true)

            timePickerDialog.show()
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

            databaseReference.child(materia).setValue(evento).addOnSuccessListener {
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