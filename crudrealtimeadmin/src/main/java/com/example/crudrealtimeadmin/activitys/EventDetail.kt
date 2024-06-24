package com.example.crudrealtimeadmin.activitys


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crudrealtimeadmin.R
import com.example.crudrealtimeadmin.items.DatoFecha
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class EventDetail : AppCompatActivity() {

    private lateinit var tvEventID: TextView
    private lateinit var tvEventMateria: TextView
    private lateinit var tvEventFecha: TextView
    private lateinit var tvEventHora: TextView
    private lateinit var tvEventTipo: TextView
    private lateinit var tvEventDescript: TextView
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

        iniciarVista()
        establecerValoresEnVista()
        btnActualizar.setOnClickListener {
            openUpdateDialog(intent.getStringExtra("IDevento").toString())
        }
    }

    private fun iniciarVista() {
        tvEventID = findViewById(R.id.tvID)
        tvEventMateria = findViewById(R.id.tvMateria)
        tvEventFecha = findViewById(R.id.tvFecha)
        tvEventHora = findViewById(R.id.tvHora)
        tvEventTipo = findViewById(R.id.tvTipo)
        tvEventDescript = findViewById(R.id.tvDescripcion)
        btnActualizar = findViewById(R.id.btn_Modificar)
        btnEliminar = findViewById(R.id.btn_Borrar)
    }

    private fun establecerValoresEnVista() {
        tvEventID.text = intent.getStringExtra("IDevento")
        tvEventMateria.text = intent.getStringExtra("evMateria")
        tvEventFecha.text = intent.getStringExtra("evFecha")
        tvEventHora.text = intent.getStringExtra("evHora")
        tvEventTipo.text = intent.getStringExtra("evTipo")
        tvEventDescript.text = intent.getStringExtra("evDescripcion")
    }

    private fun openUpdateDialog(iDevento: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        // Configurar los Spinners
        val sp_Materia = mDialogView.findViewById<Spinner>(R.id.spMaterias)
        val sp_Tipo = mDialogView.findViewById<Spinner>(R.id.spTipo)

        val listaMaterias = arrayOf("Seleccione materia", "PPI", "Testing", "Mobile", "TIC", "Taller de comunicación")
        val adaptMaterias = ArrayAdapter(this, R.layout.item_spinner, listaMaterias)
        val listaTipos = arrayOf("Seleccione tipo", "Examen", "Trabajo práctico", "Otro")
        val adaptTipos = ArrayAdapter(this, R.layout.item_spinner, listaTipos)

        sp_Materia.adapter = adaptMaterias
        sp_Tipo.adapter = adaptTipos

        // Configurar campos de fecha y hora
        val et_Fecha = mDialogView.findViewById<EditText>(R.id.etFecha)
        val et_Hora = mDialogView.findViewById<EditText>(R.id.etHora)
        val et_Descript = mDialogView.findViewById<EditText>(R.id.etNameExamen)
        val btn_updt = mDialogView.findViewById<Button>(R.id.btnActualizarDatos)

        et_Fecha.setText(intent.getStringExtra("evFecha").toString())
        et_Hora.setText(intent.getStringExtra("evHora").toString())
        et_Descript.setText(intent.getStringExtra("evDescripcion").toString())

        et_Fecha.setOnClickListener { mostrarSelectorFecha(et_Fecha) }
        et_Hora.setOnClickListener { mostrarSelectorHora(et_Hora) }

        // Obtener los valores del Intent y establecerlos en los Spinners
        val evMateria = intent.getStringExtra("evMateria")
        val evTipo = intent.getStringExtra("evTipo")

        evMateria?.let {
            val position = adaptMaterias.getPosition(it)
            if (position >= 0) {
                sp_Materia.setSelection(position)
            }
        }

        evTipo?.let {
            val position = adaptTipos.getPosition(it)
            if (position >= 0) {
                sp_Tipo.setSelection(position)
            }
        }

        mDialog.setView(mDialogView)
        mDialog.setTitle("Modificación de Evento")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btn_updt.setOnClickListener {
            actualizarDatosEvento(
                iDevento,
                sp_Materia.selectedItem.toString(),
                et_Fecha.text.toString(),
                et_Hora.text.toString(),
                sp_Tipo.selectedItem.toString(),
                et_Descript.text.toString()
            )

            // Confirmación de guardado
            Toast.makeText(applicationContext, "Datos actualizados", Toast.LENGTH_LONG).show()

            // Actualización de vista con datos ya modificados
            tvEventMateria.text = sp_Materia.selectedItem.toString()
            tvEventFecha.text = et_Fecha.text.toString()
            tvEventHora.text = et_Hora.text.toString()
            tvEventTipo.text = sp_Tipo.selectedItem.toString()
            tvEventDescript.text = et_Descript.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun mostrarSelectorFecha(et_Fecha: EditText) {
        val calendar = Calendar.getInstance()
        val anio = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            et_Fecha.setText(String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year))
        }, anio, mes, dia)

        // Configurar el mínimo permitido como la fecha actual
        datePickerDialog.datePicker.minDate = calendar.timeInMillis

        datePickerDialog.show()
    }

    private fun mostrarSelectorHora(et_Hora: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            et_Hora.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun actualizarDatosEvento(
        id: String,
        materia: String,
        fecha: String,
        hora: String,
        tipo: String,
        descripcion: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("evento").child(id)
        val infoEvent = DatoFecha(id, materia, fecha, hora, descripcion, tipo)
        dbRef.setValue(infoEvent)
    }
}
