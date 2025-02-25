package mx.edu.utng.crudtelefonos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // Se incrementó la versión para actualizar la estructura en caso de cambios
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "PhoneStoreDB"
        private const val TABLE_PHONES = "PhoneTable"
        private const val KEY_ID = "id"
        private const val KEY_BRAND = "brand"
        private const val KEY_MODEL = "model"
        private const val KEY_PRICE = "price"
        private const val KEY_STORAGE = "storage"
        private const val KEY_COLOR = "color"
        private const val KEY_RATING = "rating"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_PHONES ("
                + "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$KEY_BRAND TEXT, "
                + "$KEY_MODEL TEXT, "
                + "$KEY_PRICE REAL, "
                + "$KEY_STORAGE TEXT, "
                + "$KEY_COLOR TEXT, "
                + "$KEY_RATING REAL)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PHONES")
        onCreate(db)
    }

    // Insertar un teléfono
    fun addPhone(phone: PhoneModelClass): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_BRAND, phone.brand)
            put(KEY_MODEL, phone.model)
            put(KEY_PRICE, phone.price)
            put(KEY_STORAGE, phone.storage)
            put(KEY_COLOR, phone.color)
            put(KEY_RATING, phone.rating)
        }
        val success = db.insert(TABLE_PHONES, null, values)
        db.close()
        return success
    }

    // Obtener todos los teléfonos
    fun getAllPhones(): List<PhoneModelClass> {
        val phoneList = ArrayList<PhoneModelClass>()
        val selectQuery = "SELECT * FROM $TABLE_PHONES"
        val db = readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                // Se usan getColumnIndexOrThrow para detectar si falta alguna columna
                val idIndex = cursor.getColumnIndexOrThrow(KEY_ID)
                val brandIndex = cursor.getColumnIndexOrThrow(KEY_BRAND)
                val modelIndex = cursor.getColumnIndexOrThrow(KEY_MODEL)
                val priceIndex = cursor.getColumnIndexOrThrow(KEY_PRICE)
                val storageIndex = cursor.getColumnIndexOrThrow(KEY_STORAGE)
                val colorIndex = cursor.getColumnIndexOrThrow(KEY_COLOR)
                val ratingIndex = cursor.getColumnIndexOrThrow(KEY_RATING)

                val phone = PhoneModelClass(
                    id = cursor.getInt(idIndex),
                    brand = cursor.getString(brandIndex),
                    model = cursor.getString(modelIndex),
                    price = cursor.getDouble(priceIndex),
                    storage = cursor.getString(storageIndex),
                    color = cursor.getString(colorIndex),
                    rating = cursor.getFloat(ratingIndex)
                )
                phoneList.add(phone)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return phoneList
    }

    // Actualizar un teléfono
    fun updatePhone(phone: PhoneModelClass): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(KEY_BRAND, phone.brand)
            put(KEY_MODEL, phone.model)
            put(KEY_PRICE, phone.price)
            put(KEY_STORAGE, phone.storage)
            put(KEY_COLOR, phone.color)
            put(KEY_RATING, phone.rating)
        }
        val success = db.update(TABLE_PHONES, values, "$KEY_ID=?", arrayOf(phone.id.toString()))
        db.close()
        return success
    }

    // Eliminar un teléfono
    fun deletePhone(id: Int): Int {
        val db = writableDatabase
        val success = db.delete(TABLE_PHONES, "$KEY_ID=?", arrayOf(id.toString()))
        db.close()
        return success
    }
}