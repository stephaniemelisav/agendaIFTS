
package com.example.crudrealtimeadmin.activitys

import EventoAdapter
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.R
import com.example.crudrealtimeadmin.databinding.ActivityUploadBinding
import com.example.crudrealtimeadmin.items.DatePickerFragment
import com.example.crudrealtimeadmin.items.DatoFecha
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var eventAdapter: EventoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_upload)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)


        databaseReference = FirebaseDatabase.getInstance().getReference("evento")

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@UploadActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        })


        val materias: Spinner = binding.spMaterias
        val tipos: Spinner = binding.spTipo

        val listaMaterias = arrayOf("Seleccione materia", "Materia 01", "Materia 02", "Materia 03", "Materia 04", "Materia 05")
        val adaptMaterias = ArrayAdapter(this, R.layout.item_spinner, listaMaterias)
        val listaTipos = arrayOf("Seleccione tipo", "Examen", "Trabajo práctico", "Otro")
        val adaptTipos = ArrayAdapter(this, R.layout.item_spinner, listaTipos)

        materias.adapter = adaptMaterias
        tipos.adapter = adaptTipos

        binding.etFecha.setOnClickListener { mostrarSelectorFecha() }

        binding.etHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                binding.etHora.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
            }, hour, minute, true)

            timePickerDialog.show()
        }

        binding.saveButton.setOnClickListener {
            val materiaSeleccionada: String? = if (materias.selectedItemPosition == 0) null else materias.selectedItem.toString()
            val tipoSeleccionado: String? = if (tipos.selectedItemPosition == 0) null else tipos.selectedItem.toString()
            val fecha = binding.etFecha.text.toString()
            val hora = binding.etHora.text.toString()
            val nombreExamen = binding.etNameExamen.text.toString()

            val evento = DatoFecha(materiaSeleccionada, fecha, hora, nombreExamen, tipoSeleccionado)

            val uniqueKey = databaseReference.push().key

            if (uniqueKey != null) {
                databaseReference.child(uniqueKey).setValue(evento).addOnSuccessListener {
                    binding.etFecha.text.clear()
                    binding.etHora.text.clear()
                    binding.etNameExamen.text.clear()

                    Toast.makeText(this, "Evento guardado.", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Operación fallida: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("UploadActivity", "Error al guardar el evento", e)
                }
            } else {
                Toast.makeText(this, "Error al generar ID único para el evento", Toast.LENGTH_LONG).show()
            }
        }
        binding.viewButton.setOnClickListener {
            val intent = Intent(this@UploadActivity, ViewActivity::class.java)
            startActivity(intent)
        }

    }



    private fun mostrarSelectorFecha() {
        val datePicker = DatePickerFragment { dia, mes, anio -> enLaFechaElegida(dia, mes, anio) }
        datePicker.show(supportFragmentManager, "Selector fecha")
    }

    @SuppressLint("SetTextI18n")
    private fun enLaFechaElegida(dia: Int, mes: Int, anio: Int) {
        binding.etFecha.setText("$dia-$mes-$anio")
    }
}
