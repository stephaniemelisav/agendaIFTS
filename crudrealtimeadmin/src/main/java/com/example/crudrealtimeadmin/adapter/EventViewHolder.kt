package com.example.crudrealtimeadmin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtimeadmin.R
import com.example.crudrealtimeadmin.items.DatoFecha

class EventoAdapter(private val empEventos: ArrayList<DatoFecha>) :
    RecyclerView.Adapter<EventoAdapter.EventViewHolder>() {


        private lateinit var mListener: onItemClickListener

        // Hacer seleccionable a los items de la lista
        interface onItemClickListener{
            fun onItemClick(position: Int)
        }

        fun setOnClickListener(clickListener:onItemClickListener){
            mListener = clickListener
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentItem = empEventos[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return empEventos.size
    }

    class EventViewHolder(itemView: View, clickListener:onItemClickListener ) : RecyclerView.ViewHolder(itemView) {
        private val materiaTextView: TextView = itemView.findViewById(R.id.tvMateria)
        private val fechaTextView: TextView = itemView.findViewById(R.id.tvFecha)
        private val horaTextView: TextView = itemView.findViewById(R.id.tvHora)

        fun bind(evento: DatoFecha) {
            materiaTextView.text = evento.materia
            fechaTextView.text = evento.fecha
            horaTextView.text = evento.hora
        }
        init {
            itemView.setOnClickListener{
                clickListener.onItemClick(adapterPosition)
            }
        }
    }
}