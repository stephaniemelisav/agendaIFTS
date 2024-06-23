package com.example.crudrealtimeadmin.activitys

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtimeadmin.R
import com.example.crudrealtimeadmin.adapter.EventoAdapter
import com.example.crudrealtimeadmin.items.DatoFecha
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewActivity : AppCompatActivity() {

    private lateinit var empRecyclerView: RecyclerView
    private lateinit var empEventos: ArrayList<DatoFecha>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewlist)

        empRecyclerView = findViewById(R.id.recyclerView)
        empRecyclerView.layoutManager = LinearLayoutManager(this)
        empRecyclerView.setHasFixedSize(true)

        empEventos = arrayListOf()

        getEventData()
    }

    private fun getEventData() {
        empRecyclerView.visibility = View.GONE

        dbRef = FirebaseDatabase.getInstance().getReference("evento")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                empEventos.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.children) {
                        val empData = empSnap.getValue(DatoFecha::class.java)
                        empData?.let { empEventos.add(it) }
                    }
                    val mAdapter = EventoAdapter(empEventos)
                    empRecyclerView.adapter = mAdapter

                    empRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar el error de cancelación según sea necesario
            }
        })
    }
}