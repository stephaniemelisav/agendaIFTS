package com.example.crudrealtimeadmin.activitys

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudrealtimeadmin.R

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
            openUpdateDialog(
                intent.getStringExtra("IDevento").toString(),
                intent.getStringExtra("evMateria").toString()
            )
        }
    }

    private fun iniciarVista(){
        tvEventID = findViewById(R.id.tvID)
        tvEventMateria = findViewById(R.id.tvMateria)
        tvEventFecha = findViewById(R.id.tvFecha)
        tvEventHora = findViewById(R.id.tvHora)
        tvEventTipo = findViewById(R.id.tvTipo)
        tvEventDescript = findViewById(R.id.tvDescripcion)
        btnActualizar = findViewById(R.id.btn_Modificar)
        btnEliminar = findViewById(R.id.btn_Borrar)

    }
    private fun establecerValoresEnVista(){
        tvEventID.text = intent.getStringExtra("IDevento")
        tvEventMateria.text = intent.getStringExtra("evMateria")
        tvEventFecha.text = intent.getStringExtra("evFecha")
        tvEventHora.text = intent.getStringExtra("evHora")
        tvEventTipo.text = intent.getStringExtra("evTipo")
        tvEventDescript.text = intent.getStringExtra("evDescripcion")

    }

    private fun openUpdateDialog(iDevento: String, evmateria:String){

    }
}