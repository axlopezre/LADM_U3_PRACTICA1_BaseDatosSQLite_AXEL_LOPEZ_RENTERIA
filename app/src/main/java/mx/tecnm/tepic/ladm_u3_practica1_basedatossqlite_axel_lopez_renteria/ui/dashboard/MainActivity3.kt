package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.databinding.ActivityMain2Binding
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.databinding.ActivityMain3Binding
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home.Automovil

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    var IDARRENDA = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        IDARRENDA = this.intent.extras!!.getString("idarrenda")!!
        val arrendamiento = Arrendamiento(this).buscarArrendamientolPorID(IDARRENDA)
        binding.nombre.setText(arrendamiento.nombre)
        binding.domicilio.setText(arrendamiento.domicilio)
        binding.licencia.setText(arrendamiento.licenciaconducir)
        binding.modelo.setText(arrendamiento.modelo)
        binding.marca.setText(arrendamiento.marca)
        binding.actualizar.setOnClickListener {
            var arrendamiento = Arrendamiento(this)

            arrendamiento.nombre = binding.nombre.text.toString()
            arrendamiento.domicilio = binding.domicilio.text.toString()
            arrendamiento.licenciaconducir = binding.licencia.text.toString()
            arrendamiento.modelo = binding.modelo.text.toString()
            arrendamiento.marca = binding.marca.text.toString()

            val respuesta = arrendamiento.actualizar(IDARRENDA)
            if(respuesta){
                Toast.makeText(this, "SE ACTUALIZO CORRECTAMENTE", Toast.LENGTH_LONG)
                    .show()
                binding.nombre.setText("")
                binding.domicilio.setText("")
                binding.licencia.setText("")
                binding.modelo.setText("")
                binding.marca.setText("")
            }else{
                AlertDialog.Builder(this)
                    .setTitle("ATENCIÓN")
                    .setMessage("ERROR NO SE ACTUALIZO \nPOSIBLE CAUSA: NO EXISTE EL AUTO A ACTUALIZAR")
                    .show()
            }
        }

        binding.regresar.setOnClickListener {
            finish()
        }

    }
}