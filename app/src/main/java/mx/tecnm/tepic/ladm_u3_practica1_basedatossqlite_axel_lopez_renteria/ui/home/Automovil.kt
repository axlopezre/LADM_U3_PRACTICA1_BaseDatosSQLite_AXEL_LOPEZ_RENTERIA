package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.BaseDatos

class Automovil(este: Context) {
    private var este = este
    var idauto=0
    var modelo = ""
    var marca = ""
    var kilometrage = 0
    private var err=""

    fun insertar() : Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            var datos = ContentValues()

            datos.put("MODELO", modelo)
            datos.put("MARCA", marca)
            datos.put("KILOMETRAGE", kilometrage)

            val respuesta = tabla.insert("AUTOMOVIL", null, datos)
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

    fun mostrarTodos() : ArrayList<Automovil>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        var arreglo = ArrayList<Automovil>()
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AUTOMOVIL"

            var cursor = tabla.rawQuery(SQLSELECT, null)//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                do {
                    val automovil = Automovil(este)
                    automovil.idauto = cursor.getInt(0)
                    automovil.modelo = cursor.getString(1)
                    automovil.marca = cursor.getString(2)
                    automovil.kilometrage = cursor.getInt(3)
                    arreglo.add(automovil)
                }while (cursor.moveToNext())
            }

        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return arreglo
    }

    fun buscarAutomovilPorID(IDAUTOO: String): Automovil{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        val automovil = Automovil(este)//solo obtiene 1

        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AUTOMOVIL WHERE IDAUTO=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(IDAUTOO))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                automovil.modelo = cursor.getString(1)
                automovil.marca = cursor.getString(2)
                automovil.kilometrage = cursor.getInt(3)
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return automovil
    }

    fun buscarAutomovilPormarca(Marca: String): ArrayList<String>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        var arreglo = ArrayList<String>()
        var arreglo2 = ArrayList<Int>()
        var cadena = ArrayList<String>()
        var cadena2=""
        var i = 0
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AUTOMOVIL WHERE MARCA=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Marca))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                do {
                    arreglo.add(cursor.getString(1))
                    arreglo2.add(cursor.getInt(3))
                    cadena2 = arreglo[i] +"  "+arreglo2[i]
                    cadena.add(cadena2)
                    i++
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return cadena
    }

    fun buscarAutomovilPormodelo(Modelo: String): ArrayList<String>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        var arreglo = ArrayList<String>()
        var arreglo2 = ArrayList<Int>()
        var cadena = ArrayList<String>()
        var cadena2=""
        var i = 0
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AUTOMOVIL WHERE MODELO=?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(Modelo))//El segundo parametro es null porque no hay un where
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                do {
                    arreglo.add(cursor.getString(2))
                    arreglo2.add(cursor.getInt(3))
                    cadena2 = arreglo[i] +"  "+arreglo2[i]
                    cadena.add(cadena2)
                    i++
                    //automovil.modelo = cursor.getString(1)
                    //automovil.kilometrage = cursor.getInt(3)
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return cadena
    }

    fun buscarAutomovilPorkilometrage(kilometrage: Int, kilometrage2: Int): ArrayList<String>{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        var arreglo = ArrayList<String>()
        var arreglo2 = ArrayList<String>()
        var cadena = ArrayList<String>()
        var cadena2=""
        var i = 0
        try {
            var tabla = basedatos.readableDatabase
            val SQLSELECT = "SELECT * FROM AUTOMOVIL WHERE KILOMETRAGE BETWEEN ? AND ?"
            var cursor = tabla.rawQuery(SQLSELECT, arrayOf(kilometrage.toString(), kilometrage2.toString()))
            if(cursor.moveToFirst()) {//si huvo resultados entonces entra
                do {
                    arreglo.add(cursor.getString(1))
                    arreglo2.add(cursor.getString(2))
                    cadena2 = arreglo[i] +"  "+arreglo2[i]
                    cadena.add(cadena2)
                    i++
                }while (cursor.moveToNext())
            }
        }catch (err:SQLiteException){
            this.err = err.message!!
        }finally {
            basedatos.close()
        }
        return cadena
    }

    fun eliminar(IDAUTOO : String):Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            var tabla = basedatos.writableDatabase
            val resultado = tabla.delete("AUTOMOVIL", "IDAUTO=?", arrayOf(IDAUTOO))
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
    fun actualizar(IDAUTOO : String) : Boolean{
        val basedatos = BaseDatos(este, "BD_ARRENDAMIENTO_AUTO", null, 1)
        err = ""
        try {
            val tabla = basedatos.writableDatabase
            val datosActualizados = ContentValues()

            datosActualizados.put("MODELO", modelo)
            datosActualizados.put("MARCA", marca)
            datosActualizados.put("KILOMETRAGE", kilometrage)
            val respuesta = tabla.update("AUTOMOVIL", datosActualizados, "IDAUTO=?", arrayOf(IDAUTOO))

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

}