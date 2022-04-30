package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.dashboard

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.BaseDatos
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home.Automovil
import java.text.SimpleDateFormat
import java.util.*
import java.time.Instant
import java.time.ZoneId

class Arrendamiento(este: Context) {
    private var este = este
    var idarrenda=0
    var nombre = ""
    var domicilio = ""
    var licenciaconducir = ""
    var idauto=0
    var fecha=""
    var marca =""
    var modelo =""
    private var err=""
    fun insertar(MARCA: String, MODELO: String) : Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()
            var SQL_SELECT = "SELECT IDAUTO FROM AUTOMOVIL WHERE MODELO = ? AND MARCA = ?"
            var cursor = tabla.rawQuery(SQL_SELECT, arrayOf(MODELO, MARCA))
            if(cursor.moveToFirst()){
                idauto = cursor.getInt(0)
            }else{
                return false
            }
            datos.put("NOMBRE", nombre)
            datos.put("DOMICILIO", domicilio)
            datos.put("LICENCIACOND", licenciaconducir)
            datos.put("IDAUTO",idauto)
            var fechaActual=Instant.now()
            val mexico = fechaActual.atZone(ZoneId.of("America/Mazatlan")).toString()
            val split = mexico.split("T")
            fecha = split[0]
            datos.put("FECHA",fecha)
            var respuesta = tabla.insert("ARRENDAMIENTO", "IDARRENDA", datos)
            if(respuesta == -1L){
                return false
            }
        }catch (err: SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }
    fun mostrarTodos() : ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO"

            var cursor = tabla.rawQuery(SQLSELECT, null)//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                do {
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }

        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }

    fun buscarArrendamientolPorID(idArrendamiento: String): Arrendamiento{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        val arrendamiento = Arrendamiento(este)//solo obtiene 1

        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE IDARRENDA=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(idArrendamiento))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                arrendamiento.idarrenda = cursor.getInt(0)
                arrendamiento.nombre = cursor.getString(1)
                arrendamiento.domicilio = cursor.getString(2)
                arrendamiento.licenciaconducir = cursor.getString(3)
                arrendamiento.idauto = cursor.getInt(4)
                var auto = Automovil(este).buscarAutomovilPorID(cursor.getInt(4).toString())
                arrendamiento.marca = auto.marca
                arrendamiento.modelo = auto.modelo
                arrendamiento.fecha = cursor.getString(5)
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arrendamiento
    }
    fun eliminar(IDARRENDA : String):Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            var tabla = basedatos.writableDatabase
            val resultado = tabla.delete("ARRENDAMIENTO", "IDARRENDA=?", arrayOf(IDARRENDA))
            if(resultado == 0){
                return false
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }
    fun actualizar(IDARRENDA : String) : Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            val datosActualizados = ContentValues()

            var SQL_SELECT = "SELECT IDAUTO FROM AUTOMOVIL WHERE MODELO = ? AND MARCA = ?"
            var cursor = tabla.rawQuery(SQL_SELECT, arrayOf(modelo, marca))

            if(cursor.moveToFirst()){
                idauto = cursor.getInt(0)
            }else{
                return false
            }
            datosActualizados.put("NOMBRE", nombre)
            datosActualizados.put("DOMICILIO", domicilio)
            datosActualizados.put("LICENCIACOND", licenciaconducir)
            datosActualizados.put("IDAUTO", idauto)
            var fechaActual=Instant.now()
            val mexico = fechaActual.atZone(ZoneId.of("America/Mazatlan")).toString()
            val split = mexico.split("T")
            fecha = split[0]
            datosActualizados.put("FECHA", fecha)

            val respuesta = tabla.update("ARRENDAMIENTO", datosActualizados, "IDARRENDA=?", arrayOf(IDARRENDA))

            if(respuesta==0){
                return false
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
            return false
        }finally {
            basedatos.close()
        }
        return true
    }
    fun buscarArrendamientolPorNombre(Nombre: String): ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE NOMBRE=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Nombre))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {
                do {//si huvo resultados entonces entra
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }
    fun buscarArrendamientolPorLicencia(Licencia: String): ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE LICENCIACOND=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Licencia))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {
                do {
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }

    fun buscarArrendamientolPorDomicilio(Domicilio: String): ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE DOMICILIO=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Domicilio))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {
                do {
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }
    fun buscarArrendamientolPorModelo(Modelo: String): ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE IDAUTO IN (SELECT IDAUTO FROM AUTOMOVIL WHERE MODELO=?)"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Modelo))
            if(cursor.moveToFirst()) {
                do {
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }
    fun buscarArrendamientolPorMarca(Marca: String): ArrayList<Arrendamiento>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Arrendamiento>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM ARRENDAMIENTO WHERE IDAUTO IN (SELECT IDAUTO FROM AUTOMOVIL WHERE MARCA=?)"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Marca))
            if(cursor.moveToFirst()) {
                do {
                    val arrendamientoxd = Arrendamiento(este)
                    arrendamientoxd.idarrenda = cursor.getInt(0)
                    arrendamientoxd.nombre = cursor.getString(1)
                    arrendamientoxd.domicilio = cursor.getString(2)
                    arrendamientoxd.licenciaconducir = cursor.getString(3)
                    arrendamientoxd.idauto = cursor.getInt(4)
                    arrendamientoxd.fecha = cursor.getString(5)
                    arrendamientoxd.marca = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).marca
                    arrendamientoxd.modelo = Automovil(este).buscarAutomovilPorID(arrendamientoxd.idauto.toString()).modelo
                    arreglo.add(arrendamientoxd)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }
}