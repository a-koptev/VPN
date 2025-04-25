package com.example.vpn

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "VpnServers"
private const val DATABASE_VERSION = 1
private const val TABLE_NAME = "Information"
private const val COLUMN_ID = "id"
private const val COLUMN_COUNTRY = "country"
private const val COLUMN_CITY = "city"
private const val COLUMN_IP = "ip"
private const val COLUMN_LATITUDE = "latitude"
private const val COLUMN_LONGITUDE = "longitude"
private const val COLUMN_FLAG_RESOURCE_ID = "flag_resource_id"

data class VpnServer(
    val id: Int,
    val country: String,
    val city: String,
    val ip: String,
    val latitude: Double,
    val longitude: Double,
    val flagResourceId: Int
)

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_COUNTRY TEXT NOT NULL,
                $COLUMN_CITY TEXT NOT NULL,
                $COLUMN_IP TEXT NOT NULL,
                $COLUMN_LATITUDE DOUBLE NOT NULL,
                $COLUMN_LONGITUDE DOUBLE NOT NULL,
                $COLUMN_FLAG_RESOURCE_ID INTEGER NOT NULL
            )
        """
        db.execSQL(createTableQuery)

        insertInitialData(db)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // CREATE
    fun insertData(
        country: String,
        city: String,
        ip: String,
        latitude: Double,
        longitude: Double,
        flagResourceId: Int
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COUNTRY, country)
            put(COLUMN_CITY, city)
            put(COLUMN_IP, ip)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_FLAG_RESOURCE_ID, flagResourceId)
        }
        return db.insert(TABLE_NAME, null, values)
    }


    // READ
    fun getAllData(): MutableList<HashMap<String, String>> {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val dataList = mutableListOf<HashMap<String, String>>()

        with(cursor) {
            while (moveToNext()) {
                val id = getString(getColumnIndexOrThrow(COLUMN_ID))
                val country = getString(getColumnIndexOrThrow(COLUMN_COUNTRY))
                val city = getString(getColumnIndexOrThrow(COLUMN_CITY))
                val ip = getString(getColumnIndexOrThrow(COLUMN_CITY))
                val latitude = getDouble(getColumnIndexOrThrow(COLUMN_LATITUDE))
                val longitude = getDouble(getColumnIndexOrThrow(COLUMN_LONGITUDE))
                val flagResourceId = getDouble(getColumnIndexOrThrow(COLUMN_FLAG_RESOURCE_ID))
                val map = HashMap<String, String>()
                map[COLUMN_ID] = id
                map[COLUMN_COUNTRY] = country
                map[COLUMN_CITY] = city
                map[COLUMN_IP] = ip
                map[COLUMN_LATITUDE] = latitude.toString()
                map[COLUMN_LONGITUDE] = longitude.toString()
                map[COLUMN_FLAG_RESOURCE_ID] = flagResourceId.toString()

                dataList.add(map)
            }
        }
        cursor.close()
        return dataList
    }

    fun getDataById(id: Int): VpnServer? {
        val db = readableDatabase
        var vpnServer: VpnServer? = null

        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                vpnServer = VpnServer(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    country = it.getString(it.getColumnIndexOrThrow(COLUMN_COUNTRY)),
                    city = it.getString(it.getColumnIndexOrThrow(COLUMN_CITY)),
                    ip = it.getString(it.getColumnIndexOrThrow(COLUMN_IP)),
                    latitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                    longitude = it.getDouble(it.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                    flagResourceId = it.getInt(it.getColumnIndexOrThrow(COLUMN_FLAG_RESOURCE_ID))
                )
            }
        }

        db.close()
        return vpnServer
    }

    // UPDATE
    fun updateData(
        id: Long,
        country: String,
        city: String,
        ip: String,
        latitude: Double,
        longitude: Double,
        flagResourceId: Int
    ): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COUNTRY, country)
            put(COLUMN_CITY, city)
            put(COLUMN_IP, ip)
            put(COLUMN_LATITUDE, latitude)
            put(COLUMN_LONGITUDE, longitude)
            put(COLUMN_FLAG_RESOURCE_ID, flagResourceId)
        }
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    // DELETE
    fun deleteData(id: Long): Int {
        val db = writableDatabase
        val selection = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(id.toString())
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }


    private fun insertInitialData(db: SQLiteDatabase) {
        db.execSQL("INSERT INTO $TABLE_NAME ($COLUMN_COUNTRY, $COLUMN_CITY, $COLUMN_IP, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_FLAG_RESOURCE_ID) " +
                "VALUES ('Япония', 'Токио', '219.100.37.96', 35.681729, 139.753927, ${R.drawable.flag_jp})")
        db.execSQL(
            "INSERT INTO $TABLE_NAME ($COLUMN_COUNTRY, $COLUMN_CITY, $COLUMN_IP, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_FLAG_RESOURCE_ID) " +
                    "VALUES ('Польша', 'Варшава', '213.189.63.248', 52.232625, 21.009286, ${R.drawable.flag_pl})"
        )
        db.execSQL(
            "INSERT INTO $TABLE_NAME ($COLUMN_COUNTRY, $COLUMN_CITY, $COLUMN_IP, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_FLAG_RESOURCE_ID) " +
                    "VALUES ('Чехия', 'Острава', '128.0.190.10', 49.836192, 18.277994, ${R.drawable.flag_cz})"
        )
        db.execSQL(
            "INSERT INTO $TABLE_NAME ($COLUMN_COUNTRY, $COLUMN_CITY, $COLUMN_IP, $COLUMN_LATITUDE, $COLUMN_LONGITUDE, $COLUMN_FLAG_RESOURCE_ID) " +
                    "VALUES ('Германия', 'Дюссельдорф', '62.133.35.246', 51.230569, 6.787428, ${R.drawable.flag_de})"
        )
    }

    fun getIdByCoordinates(latitude: Double, longitude: Double): Int? {
        val db = readableDatabase
        var id: Int? = null

        val query =
            "SELECT $COLUMN_ID FROM $TABLE_NAME WHERE $COLUMN_LATITUDE = ? AND $COLUMN_LONGITUDE = ?"
        val cursor = db.rawQuery(query, arrayOf(latitude.toString(), longitude.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
            }
        }

        db.close()

        return id
    }
}