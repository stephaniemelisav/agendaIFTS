package com.example.crudrealtimeadmin

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

        //____________Menú spinner para Materia y tipos_____________
        val materias : Spinner = binding.spMaterias
        val tipos :Spinner = binding.spTipo
        // ---------Harcodeo lista materias
        val listaMaterias = arrayOf("Seleccione materia", "Materia 01","Materia 02","Materia 03","Materia 04", "Materia 05")
        val adaptMaterias = ArrayAdapter(this, R.layout.item_spinner, listaMaterias)
        // ---------Hardcodeo lista tipos
        val listaTipos = arrayOf("Seleccione tipo" ,"Examen","Trabajo práctico","Otro")
        val adaptTipos = ArrayAdapter(this, R.layout.item_spinner,listaTipos)

        materias.adapter = adaptMaterias
        tipos.adapter = adaptTipos
        // ____________________Fin Spinners______________________

        // Selector de Fecha
        binding.etFecha.setOnClickListener{mostrarSelectorFecha()}


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
            // Elección de spinner
            // Se valida valor de posición
            val materiaSeleccionada : String? = if(materias.selectedItemPosition == 0) null else materias.selectedItem.toString()
            val tipoSeleccionado : String? = if(tipos.selectedItemPosition == 0)null else tipos.selectedItem.toString()
            //variables que guardan los datos
            val fecha = binding.etFecha.text.toString()
            val hora = binding.etHora.text.toString()
            val nombreExamen = binding.etNameExamen.text.toString()


            databaseReference = FirebaseDatabase.getInstance().getReference("evento")

            val evento = DatoFecha( materiaSeleccionada, fecha, hora, nombreExamen, tipoSeleccionado)

            databaseReference.child(fecha).setValue(evento).addOnSuccessListener {
                binding.etFecha.text.clear()
                binding.etHora.text.clear()
                binding.etNameExamen.text.clear()

                // Muestra un pequeño aviso de que se subió correctamente
                Toast.makeText(this, "Evento guardado.", Toast.LENGTH_LONG).show()
            }.addOnFailureListener { e ->
                // Mensaje en caso de falla
                Toast.makeText(this, "Operación fallida: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("UploadActivity", "Error al guardar el evento", e)
            }

        }
    }

    private fun mostrarSelectorFecha() {
        val datePicker = DatePickerFragment{dia, mes, anion -> enLaFechaElegida(dia, mes, anion)}
        datePicker.show(supportFragmentManager,"Selector fecha")
    }

    @SuppressLint("SetTextI18n")
    private fun enLaFechaElegida(dia:Int, mes:Int, anion:Int){
        binding.etFecha.setText("$dia-$mes-$anion")
    }

}