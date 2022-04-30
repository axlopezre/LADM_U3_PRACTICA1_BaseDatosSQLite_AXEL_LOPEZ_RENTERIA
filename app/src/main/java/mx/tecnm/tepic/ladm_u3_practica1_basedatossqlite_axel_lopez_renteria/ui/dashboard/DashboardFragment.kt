package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.dashboard

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.databinding.FragmentDashboardBinding
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home.Automovil
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home.MainActivity2
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    var listaIDs = ArrayList<String>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mostrarDatosEnListView()
        //CODIGO
        AlertDialog.Builder(requireContext())
            .setTitle("VENTANA INFORMATIVA")
            .setMessage("PARA CONSULTAR SE TIENE QUE AGREGAR LA INFORMACION QUE REQUIERE CON SU RESPECTIVO CUADRO DE TEXTO(1 POR 1 SOLAMENTE) Y POR CONSIGUIENTE PRESIONAR EL BOTON DE CONSULTAR\n" +
                    "\nSI NECESITAS VOLVER A MOSTRAR LOS ARRENDAMIENTOS EXISTENTES DESPUES DE CONSULTAR ES NECESARIO PRESIONAR EL BOTON MOSTRAR EXISTENTES\n" +
                    "\nPARA ELIMINAR Y ACTUALIZAR ES PRESIONAR EL ARRENDAMIENTO(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE")
            .show()
        binding.insertar.setOnClickListener {
            var arrendamientoxd = Arrendamiento(requireContext())

            arrendamientoxd.nombre = binding.nombre.text.toString()
            arrendamientoxd.domicilio = binding.domicilio.text.toString()
            arrendamientoxd.licenciaconducir = binding.licencia.text.toString()
            arrendamientoxd.modelo = binding.modelo.text.toString()
            arrendamientoxd.marca = binding.marca.text.toString()

            val resultado = arrendamientoxd.insertar(arrendamientoxd.marca, arrendamientoxd.modelo)
            if (resultado) {
                Toast.makeText(requireContext(), "SE INSERTO EXITOSAMENTE", Toast.LENGTH_LONG)
                    .show()
                mostrarDatosEnListView()
                binding.nombre.setText("")
                binding.domicilio.setText("")
                binding.licencia.setText("")
                binding.modelo.setText("")
                binding.marca.setText("")
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO INSERTAR")
                    .show()
            }//CLASE MAIN ACTIOVITY ES VISTA! ES DECIR INTERFAZ GRAFICA
        }
        binding.consultar.setOnClickListener {
            if(!binding.nombre.text.toString().equals("")) {
                val listaarrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorNombre(binding.nombre.text.toString())
                MostrarConsultaPorX(listaarrendamiento)
            }
            if(!binding.licencia.text.toString().equals("")) {
                val listaarrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorLicencia(binding.licencia.text.toString())
                MostrarConsultaPorX(listaarrendamiento)
            }
            if(!binding.domicilio.text.toString().equals("")) {
                val listaarrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorDomicilio(binding.domicilio.text.toString())
                MostrarConsultaPorX(listaarrendamiento)
            }
            if(!binding.modelo.text.toString().equals("")) {
                val listaarrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorModelo(binding.modelo.text.toString())
                MostrarConsultaPorX(listaarrendamiento)
            }
            if(!binding.marca.text.toString().equals("")) {
                val listaarrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorMarca(binding.marca.text.toString())
                MostrarConsultaPorX(listaarrendamiento)
            }
        }
        binding.mostrartodos.setOnClickListener {
            mostrarDatosEnListView()
            binding.nombre.setText("")
            binding.domicilio.setText("")
            binding.licencia.setText("")
            binding.modelo.setText("")
            binding.marca.setText("")
        }
        return root
    }
    fun mostrarDatosEnListView(){
        var listaArrendamiento = Arrendamiento(requireContext()).mostrarTodos()
        var nombreArrendamiento = ArrayList<String>()
        var domicilioArrendamiento = ArrayList<String>()
        var licenciaArrendamiento = ArrayList<String>()
        var modeloArrendamiento = ArrayList<String>()
        var marcaArrendamiento = ArrayList<String>()
        var fechaArrendamiento = ArrayList<String>()
        var cadena = ArrayList<String>()
        var cadena2=""

        listaIDs.clear()
        (0..listaArrendamiento.size-1).forEach {
            val ar = listaArrendamiento.get(it)
            nombreArrendamiento.add(ar.nombre)
            domicilioArrendamiento.add(ar.domicilio)
            licenciaArrendamiento.add(ar.licenciaconducir)
            modeloArrendamiento.add(ar.modelo)
            marcaArrendamiento.add(ar.marca)
            fechaArrendamiento.add(ar.fecha)
            listaIDs.add(ar.idarrenda.toString())
            cadena2= nombreArrendamiento[it]+" "+domicilioArrendamiento[it] +" "+licenciaArrendamiento[it]+" "+modeloArrendamiento[it]+" "+marcaArrendamiento[it] +" "+ fechaArrendamiento[it]
            cadena.add(cadena2)
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1, cadena)
        binding.lista.setOnItemClickListener { adapterView, view, indice, l ->
            val idArrenda = listaIDs.get(indice)
            val arrendamiento = Arrendamiento(requireContext()).buscarArrendamientolPorID(idArrenda)

            AlertDialog.Builder(requireContext())
                .setTitle("ATENCION")
                .setMessage("Qué deseas hacer con \nId: ${arrendamiento.idarrenda} \nNombre: ${arrendamiento.nombre} \nDomicilio: ${arrendamiento.domicilio} \nLicenciaDeConducir: ${arrendamiento.licenciaconducir}" +
                        "\nModelo: ${arrendamiento.modelo} \nMarca: ${arrendamiento.marca} \nFecha: ${arrendamiento.fecha}?")
                .setNegativeButton("Eliminar"){d,i->
                    arrendamiento.eliminar(idArrenda)
                    mostrarDatosEnListView()
                }
                .setPositiveButton("Actualizar"){d,i->
                    var otraVentana = Intent(requireContext(), MainActivity3::class.java)
                    otraVentana.putExtra("idarrenda",idArrenda)
                    startActivity(otraVentana)
                }
                .setNeutralButton("Cerrar"){d,i->}
                .show()
        }
    }
    fun MostrarConsultaPorX(listaarrendamiento: ArrayList<Arrendamiento>){
        var nombreArrendamiento = ArrayList<String>()
        var domicilioArrendamiento = ArrayList<String>()
        var licenciaArrendamiento = ArrayList<String>()
        var modeloArrendamiento = ArrayList<String>()
        var marcaArrendamiento = ArrayList<String>()
        var fechaArrendamiento = ArrayList<String>()
        var cadena = ArrayList<String>()
        var cadena2=""

        listaIDs.clear()
        (0..listaarrendamiento.size-1).forEach {
            val ar = listaarrendamiento.get(it)
            nombreArrendamiento.add(ar.nombre)
            domicilioArrendamiento.add(ar.domicilio)
            licenciaArrendamiento.add(ar.licenciaconducir)
            modeloArrendamiento.add(ar.modelo)
            marcaArrendamiento.add(ar.marca)
            fechaArrendamiento.add(ar.fecha)
            listaIDs.add(ar.idarrenda.toString())
            cadena2= nombreArrendamiento[it]+" "+domicilioArrendamiento[it] +" "+licenciaArrendamiento[it]+" "+modeloArrendamiento[it]+" "+marcaArrendamiento[it] +" "+ fechaArrendamiento[it]
            cadena.add(cadena2)
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),
            R.layout.simple_list_item_1, cadena)
    }
    override fun onResume() {
        mostrarDatosEnListView()
        super.onResume()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}