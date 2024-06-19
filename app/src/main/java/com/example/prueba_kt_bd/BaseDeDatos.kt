package com.example.prueba_kt_bd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

var BD = "baseDatos"
//num de versión es la version en la que vamos a mejorar nuestro sistema, por eso v1
class BaseDatos(context: Context) : SQLiteOpenHelper(context, BD, null, 1) {

    //orden de creación de tablas
    override fun onCreate(db: SQLiteDatabase?) {
        //variable para dar la orden de creación. Creo una tabla que se llama usuarios, le pongo un id
        //otro campo llamado nombre y edad .
        val sql = "CREATE TABLE Usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR(250), edad INTEGER)"
        //orden para que lance esto:
        db?.execSQL(sql)
    }

    //
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //orden para borrado, solo si existe.
        db?.execSQL("DROP TABLE IF EXISTS Usuario")
        onCreate(db)
    }

    // CRUD
    fun insertarDatos(usuario: Usuario): String {
        //variable que guarda el acceso a modo escritura para cambios
        val db = this.writableDatabase
        //variable para utilizar contentvalues
        val contenedorDeValores = ContentValues()

        //Ordenes para añadir tanto para nombre y edad, datos entrantes
        contenedorDeValores.put("nombre", usuario.nombre)
        contenedorDeValores.put("edad", usuario.edad)

        val res = db.insert("Usuario", null, contenedorDeValores)
        //cerramos la bs
        db.close()
        //verificamos que el ingreso no fue nulo para que salte el error.
        return if (res == -1L) {
            "Hubo un error al insertar este usuario a la base de datos"
        } else {
            "Carga exitosa de la base de datos"
        }
    }

    @SuppressLint("Range")
    fun listarDatos(): MutableList<Usuario> {
        val lista: MutableList<Usuario> = ArrayList()
        val db = this.readableDatabase
        val sql = "SELECT * FROM Usuario"
        val res = db.rawQuery(sql, null)
        //mientras exista:
        if (res.moveToFirst()) {
            do {
                //si existe, muestra los datos
                val usu = Usuario(
                    id = res.getInt(res.getColumnIndex("id")),
                    nombre = res.getString(res.getColumnIndex("nombre")),
                    edad = res.getInt(res.getColumnIndex("edad"))
                )
                lista.add(usu)
            } while (res.moveToNext())
        }
        res.close()
        db.close()
        return lista
    }

    fun actualizarDatos(id: String, nombre: String, edad: Int): String {
        val db = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("nombre", nombre)
        contenedor.put("edad", edad)

        val resultado = db.update("Usuario", contenedor, "id=?", arrayOf(id))
        db.close()
        return if (resultado > 0) {
            "Actualización exitosa"
        } else {
            "Algo falló en la actualización de datos"
        }
    }

    fun eliminarDatos(id: String): String {
        val db = this.writableDatabase
        //elimino usando array
        val resultado = db.delete("Usuario", "id=?", arrayOf(id))
        db.close()
        return if (resultado > 0) {
            "Eliminación exitosa"
        } else {
            "Algo falló en la eliminación de datos"
        }
    }
}
