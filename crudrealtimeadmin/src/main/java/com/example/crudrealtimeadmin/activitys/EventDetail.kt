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
    private lateinit var tvEventDescript: TextView
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)

    }
}