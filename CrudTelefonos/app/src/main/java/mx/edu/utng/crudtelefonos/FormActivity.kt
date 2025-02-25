package mx.edu.utng.crudtelefonos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormActivity : AppCompatActivity() {

    private lateinit var etBrand: EditText
    private lateinit var etModel: EditText
    private lateinit var etPrice: EditText
    private lateinit var etStorage: EditText
    private lateinit var etColor: EditText
    private lateinit var etRating: EditText

    private lateinit var btnSave: Button
    private lateinit var databaseHandler: DatabaseHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        etBrand = findViewById(R.id.etBrand)
        etModel = findViewById(R.id.etModel)
        etPrice = findViewById(R.id.etPrice)
        etStorage = findViewById(R.id.etStorage)
        etColor = findViewById(R.id.etColor)
        etRating = findViewById(R.id.etRating)
        btnSave = findViewById(R.id.btnSavePhone)
        databaseHandler = DatabaseHandler(this)

        // Verificar si se está en modo edición
        val phoneId = intent.getIntExtra("phoneId", 0)
        if (phoneId != 0) {
            // Modo edición: cargar datos existentes
            val phone = databaseHandler.getAllPhones().find { it.id == phoneId }
            phone?.let {
                etBrand.setText(it.brand)
                etModel.setText(it.model)
                etPrice.setText(it.price.toString())
                etStorage.setText(it.storage)
                etColor.setText(it.color)
                etRating.setText(it.rating.toString())
            }
            btnSave.text = "Actualizar Teléfono"

            btnSave.setOnClickListener {
                val brand = etBrand.text.toString()
                val model = etModel.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
                val storage = etStorage.text.toString()
                val color = etColor.text.toString()
                val rating = etRating.text.toString().toFloatOrNull() ?: 0f

                if (brand.isNotEmpty() && model.isNotEmpty()) {
                    val updatedPhone = PhoneModelClass(phoneId, brand, model, price, storage, color, rating)
                    val result = databaseHandler.updatePhone(updatedPhone)
                    if (result > -1) {
                        Toast.makeText(this, "Teléfono actualizado", Toast.LENGTH_SHORT).show()
                        // Enviar a la pantalla principal
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Marca y Modelo son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Modo agregar: comportamiento original
            btnSave.setOnClickListener {
                val brand = etBrand.text.toString()
                val model = etModel.text.toString()
                val price = etPrice.text.toString().toDoubleOrNull() ?: 0.0
                val storage = etStorage.text.toString()
                val color = etColor.text.toString()
                val rating = etRating.text.toString().toFloatOrNull() ?: 0f

                if (brand.isNotEmpty() && model.isNotEmpty()) {
                    val phone = PhoneModelClass(0, brand, model, price, storage, color, rating)
                    val status = databaseHandler.addPhone(phone)
                    if (status > -1) {
                        Toast.makeText(this, "Teléfono guardado", Toast.LENGTH_SHORT).show()
                        // Enviar a la pantalla principal
                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Marca y Modelo son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
