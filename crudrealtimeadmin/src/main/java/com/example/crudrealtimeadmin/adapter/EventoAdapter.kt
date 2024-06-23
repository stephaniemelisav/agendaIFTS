import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtimeadmin.R
import com.example.crudrealtimeadmin.items.DatoFecha

class EventoAdapter(private val empEventos: ArrayList<DatoFecha>) :
    RecyclerView.Adapter<EventoAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_evento, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Aquí debes implementar la lógica para vincular los datos con las vistas
        val datoFecha = empEventos[position]
        // Por ejemplo, puedes establecer el texto en un TextView
        // holder.itemView.findViewById<TextView>(R.id.textViewEvento).text = datoFecha.nombreExamen
    }

    override fun getItemCount(): Int {
        return empEventos.size
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí puedes inicializar vistas si es necesario
    }
}
