package mx.edu.utng.crudtelefonos

data class PhoneModelClass(
    var id: Int,            // ID único (autogenerado)
    var brand: String,      // Marca del teléfono
    var model: String,      // Modelo
    var price: Double,      // Precio
    var storage: String,    // Capacidad de almacenamiento
    var color: String,      // Color
    var rating: Float       // Calificación
)