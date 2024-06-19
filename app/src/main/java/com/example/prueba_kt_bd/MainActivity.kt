package com.example.prueba_kt_bd

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.prueba_kt_bd.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //vincular vistas: B*
    private lateinit var binding: ActivityMainBinding

    //conexión con el xml
    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var btnInsertar: Button
    private lateinit var btnListar: Button
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button
    private lateinit var lvUsuarios: ListView
    //conexión con mi base de datos A*
    private lateinit var db: BaseDatos
    private var usuarioSeleccionado: Usuario? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //*B
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicializar vistas
        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        btnInsertar = findViewById(R.id.btnInsertar)
        btnListar = findViewById(R.id.btnListar)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnEliminar = findViewById(R.id.btnEliminar)
        lvUsuarios = findViewById(R.id.lvUsuarios)

        //configurar bordes de ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //A* inicializo mi base de datos
        db = BaseDatos(this)

        //configurar listeners para los botones
        btnInsertar.setOnClickListener {
            insertarUsuario()
        }

        btnListar.setOnClickListener {
            listarUsuarios()
        }

        btnActualizar.setOnClickListener {
            actualizarUsuario()
        }

        btnEliminar.setOnClickListener {
            eliminarUsuario()
        }

        //configurar selección en ListView
        lvUsuarios.setOnItemClickListener { _, _, position, _ ->
            val usuario = lvUsuarios.adapter.getItem(position) as String
            val id = usuario.split(":")[0].toIntOrNull()
            usuarioSeleccionado = db.listarDatos().find { it.id == id }
            usuarioSeleccionado?.let {
                etNombre.setText(it.nombre)
                etEdad.setText(it.edad.toString())
            }
        }
    }
    //función que inserta un usuario en la bd
    private fun insertarUsuario() {
        val nombre = etNombre.text.toString()
        val edad = etEdad.text.toString().toIntOrNull()

        if (nombre.isNotBlank() && edad != null) {
            val usuario = Usuario(nombre = nombre, edad = edad)
            val resultado = db.insertarDatos(usuario)
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
            limpiarCampos()
            listarUsuarios()
        } else {
            Toast.makeText(this, "Por favor, ingrese nombre y edad válidos", Toast.LENGTH_SHORT).show()
        }
    }
    //listamos los usuarios en el listview que usamos en el xml
    private fun listarUsuarios() {
        val listaUsuarios = db.listarDatos()
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaUsuarios.map { "${it.id}: ${it.nombre}, ${it.edad}" })
        lvUsuarios.adapter = adaptador
    }

    //actualizamos la info existente de un usuario que seleccionamos
    private fun actualizarUsuario() {
        val nombre = etNombre.text.toString()
        val edad = etEdad.text.toString().toIntOrNull()
        val id = usuarioSeleccionado?.id

        if (id != null && nombre.isNotBlank() && edad != null) {
            val resultado = db.actualizarDatos(id.toString(), nombre, edad)
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
            limpiarCampos()
            listarUsuarios()
        } else {
            Toast.makeText(this, "Por favor, seleccione un usuario y ingrese nombre y edad válidos", Toast.LENGTH_SHORT).show()
        }
    }

    //Eliminamos el usuario seleccionado
    private fun eliminarUsuario() {
        val id = usuarioSeleccionado?.id

        if (id != null) {
            val resultado = db.eliminarDatos(id.toString())
            Toast.makeText(this, resultado, Toast.LENGTH_SHORT).show()
            limpiarCampos()
            listarUsuarios()
        } else {
            Toast.makeText(this, "Por favor, seleccione un usuario para eliminar", Toast.LENGTH_SHORT).show()
        }
    }

    //función para limpiar los campos de entrada, asi reseteamos vista
    private fun limpiarCampos() {
        etNombre.text.clear()
        etEdad.text.clear()
        usuarioSeleccionado = null
    }
}
