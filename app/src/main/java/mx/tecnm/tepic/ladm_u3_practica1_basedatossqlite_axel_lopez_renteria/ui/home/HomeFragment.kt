package mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.ui.home

import android.R
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Delay
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.BaseDatos
import mx.tecnm.tepic.ladm_u3_practica1_basedatossqlite_axel_lopez_renteria.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    var listaIDs = ArrayList<String>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        mostrarDatosEnListView()
        //CODIGO
        AlertDialog.Builder(requireContext())
            .setTitle("VENTANA INFORMATIVA")
            .setMessage("PARA CONSULTAR SE TIENE QUE AGREGAR LA INFORMACION QUE REQUIERE CON SU RESPECTIVO CUADRO DE TEXTO(1 POR 1 SOLAMENTE) Y POR CONSIGUIENTE PRESIONAR EL BOTON DE CONSULTAR\n" +
                    "\nSI NECESITAS VOLVER A MOSTRAR LOS CARROS EXISTENTES DESPUES DE CONSULTAR ES NECESARIO PRESIONAR EL BOTON MOSTRAR EXISTENTES\n" +
                    "\nPARA CONSULTAR UN RANGO DE KILOMETRAJE EL FORMATO ES: (MÍNIMO,MÁXIMO)\n" +
                    "\nPARA ELIMINAR Y ACTUALIZAR ES PRESIONAR EL AUTOMOVIL(EN EL LISTVIEW) Y TE DARÁ LAS OPCIONES AUTOMATICAMENTE")
            .show()
        binding.insertar.setOnClickListener {
            var automovil = Automovil(requireContext()) //ALUMNO ES CLASE CONTROLADOR = ADMON DE DATOS

            automovil.modelo = binding.modelo.text.toString()
            automovil.marca = binding.marca.text.toString()
            automovil.kilometrage = binding.kilometrage.text.toString().toInt()

            val resultado = automovil.insertar()//PARA EL MAIN ACTIVITY LA INSERCION ES ABSTRACTA
            if (resultado) {
                Toast.makeText(requireContext(), "SE INSERTO EXITOSAMENTE", Toast.LENGTH_LONG)
                    .show()
                //System.out.println(automovil.idauto)
                mostrarDatosEnListView()
                binding.modelo.setText("")
                binding.marca.setText("")
                binding.kilometrage.setText("")
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("ERROR")
                    .setMessage("NO SE PUDO INSERTAR")
                    .show()
            }//CLASE MAIN ACTIOVITY ES VISTA! ES DECIR INTERFAZ GRAFICA
        }
        binding.consultar.setOnClickListener {
            if(!binding.marca.text.toString().equals("")) {
                val automovil = Automovil(requireContext()).buscarAutomovilPormarca(binding.marca.text.toString())
                binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, automovil)
            }
            if(!binding.modelo.text.toString().equals("")) {
                val automovil = Automovil(requireContext()).buscarAutomovilPormodelo(binding.modelo.text.toString())
                binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, automovil)
            }
            if(!binding.kilometrage.text.toString().equals("")) {
               var kil = binding.kilometrage.text.toString()
                val dividir = kil.split(",")
                val automovil = Automovil(requireContext()).buscarAutomovilPorkilometrage(dividir[0].toInt(), dividir[1].toInt())
                binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, automovil)
            }
            if(binding.kilometrage.text.toString().equals("")&&binding.modelo.text.toString().equals("")&&binding.marca.text.toString().equals("")) {
                AlertDialog.Builder(requireContext())
                    .setTitle("ATENCIÓN")
                    .setMessage("INGRESA ALGUN DATO A BUSCAR PARA PODER CONSULTAR")
                    .setNeutralButton("Cerrar"){d,i->}
                    .show()
            }
        }
        binding.mostrartodos.setOnClickListener {
            mostrarDatosEnListView()
            binding.modelo.setText("")
            binding.marca.setText("")
            binding.kilometrage.setText("")
        }
        //
        return root
    }
    fun mostrarDatosEnListView(){
        var listaAutos = Automovil(requireContext()).mostrarTodos()
        var modeloAutomovil = ArrayList<String>()
        var marcaAutomovil = ArrayList<String>()
        var kilometrageAutomovil = ArrayList<Int>()
        var cadena = ArrayList<String>()
        var cadena2=""

        listaIDs.clear()
        (0..listaAutos.size-1).forEach {
            val au = listaAutos.get(it)
            modeloAutomovil.add(au.modelo)
            marcaAutomovil.add(au.marca)
            kilometrageAutomovil.add(au.kilometrage)
            listaIDs.add(au.idauto.toString())
            cadena2= listaIDs[it]+" "+modeloAutomovil[it]+" "+marcaAutomovil[it] +" "+kilometrageAutomovil[it]
            cadena.add(cadena2)
        }
        binding.lista.adapter = ArrayAdapter<String>(requireContext(),android.R.layout.simple_list_item_1, cadena)
        binding.lista.setOnItemClickListener { adapterView, view, indice, l ->
            val idAuto = listaIDs.get(indice)
            val automovil = Automovil(requireContext()).buscarAutomovilPorID(idAuto)

            AlertDialog.Builder(requireContext())
                .setTitle("ATENCION")
                .setMessage("Qué deseas hacer con \nModelo: ${automovil.modelo} \nMarca: ${automovil.marca} \nKilometrage: ${automovil.kilometrage}?")
                .setNegativeButton("Eliminar"){d,i->
                    automovil.eliminar(idAuto)
                    mostrarDatosEnListView()
                }
                .setPositiveButton("Actualizar"){d,i->
                    var otraVentana = Intent(requireContext(), MainActivity2::class.java)
                    otraVentana.putExtra("idauto",idAuto)
                    startActivity(otraVentana)
                }
                .setNeutralButton("Cerrar"){d,i->}
                .show()
        }
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