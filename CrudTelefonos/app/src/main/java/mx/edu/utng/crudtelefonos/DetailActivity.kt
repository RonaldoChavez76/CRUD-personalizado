package mx.edu.utng.crudtelefonos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private var phoneId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        databaseHandler = DatabaseHandler(this)
        phoneId = intent.getIntExtra("phoneId", 0)

        // Obtener el teléfono a partir del ID
        val phone = databaseHandler.getAllPhones().find { it.id == phoneId }

        val tvBrand = findViewById<TextView>(R.id.detailBrand)
        val tvModel = findViewById<TextView>(R.id.detailModel)
        val tvPrice = findViewById<TextView>(R.id.detailPrice)
        val tvStorage = findViewById<TextView>(R.id.detailStorage)
        val tvColor = findViewById<TextView>(R.id.detailColor)
        val tvRating = findViewById<TextView>(R.id.detailRating)

        phone?.let {
            tvBrand.text = "Marca: ${it.brand}"
            tvModel.text = "Modelo: ${it.model}"
            tvPrice.text = "Precio: $${it.price}"
            tvStorage.text = "Almacenamiento: ${it.storage}"
            tvColor.text = "Color: ${it.color}"
            tvRating.text = "Calificación: ${it.rating}"
        }

        // Configurar botón de editar
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        btnEdit.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            // Pasar el id para que FormActivity sepa que es edición
            intent.putExtra("phoneId", phoneId)
            startActivity(intent)
        }

        // Configurar botón de eliminar
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Teléfono")
                .setMessage("¿Estás seguro de que deseas eliminar este teléfono?")
                .setPositiveButton("Sí") { _, _ ->
                    val result = databaseHandler.deletePhone(phoneId)
                    if (result > 0) {
                        Toast.makeText(this, "Teléfono eliminado", Toast.LENGTH_SHORT).show()
                        finish() // Regresa a MainActivity
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}