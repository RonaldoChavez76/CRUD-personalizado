package mx.edu.utng.crudtelefonos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), PhoneAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var phoneAdapter: PhoneAdapter
    private lateinit var phoneList: ArrayList<PhoneModelClass>
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHandler = DatabaseHandler(this)
        phoneList = ArrayList(databaseHandler.getAllPhones())

        recyclerView = findViewById(R.id.recyclerViewPhones)
        recyclerView.layoutManager = LinearLayoutManager(this)
        phoneAdapter = PhoneAdapter(phoneList, this)
        recyclerView.adapter = phoneAdapter

        // Configurar SearchView para filtrar en tiempo real
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                phoneAdapter.filter.filter(newText)
                return false
            }
        })

        // Abrir formulario para agregar un nuevo teléfono
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        fab.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
    }

    // Al hacer clic en un ítem se abre la pantalla de detalle
    override fun onItemClick(position: Int) {
        val selectedPhone = phoneList[position]
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("phoneId", selectedPhone.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Limpiar la lista y agregar los datos actualizados desde la base de datos
        phoneList.clear()
        phoneList.addAll(databaseHandler.getAllPhones())
        phoneAdapter.notifyDataSetChanged()
    }

}