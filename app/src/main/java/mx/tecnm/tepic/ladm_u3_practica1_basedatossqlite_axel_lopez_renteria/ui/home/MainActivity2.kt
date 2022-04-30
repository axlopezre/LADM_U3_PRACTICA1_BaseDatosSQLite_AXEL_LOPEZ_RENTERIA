package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.R
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    var IDAUTOO = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        IDAUTOO = this.intent.extras!!.getString("idauto")!!
        val automovil = Automovil(this).buscarAutomovilPorID(IDAUTOO)

        binding.modelo.setText(automovil.modelo)
        binding.marca.setText(automovil.marca)
        binding.kilometrage.setText(automovil.kilometrage.toString())

        binding.actualizar.setOnClickListener {
            var automovil = Automovil(this)

            automovil.modelo = binding.modelo.text.toString()
            automovil.marca = binding.marca.text.toString()
            automovil.kilometrage = binding.kilometrage.text.toString().toInt()

            val respuesta = automovil.actualizar(IDAUTOO)
            if(respuesta){
                Toast.makeText(this, "SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG)
                    .show()
                binding.modelo.setText("")
                binding.marca.setText("")
                binding.kilometrage.setText("")
            }else{
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("ERROR NO SE ACTUALIZO")
                    .show()
            }
        }

        binding.regresar.setOnClickListener {
            finish()
        }
    }
}