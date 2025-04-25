package com.example.vpn

import android.graphics.PointF
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider


class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var selectedPlaceMark: PlacemarkMapObject


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Utils.mapIsInit) {
            MapKitFactory.setApiKey("f2e06fdb-9688-4224-9a52-83ccb2f715c2")
            MapKitFactory.initialize(this)
            Utils.mapIsInit = true
        }


        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainConstraint)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<LinearLayout>(R.id.defaultServer).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.acceptButton).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.map_close_butt).setOnClickListener { finish() }

        val acceptButton: ImageButton = findViewById(R.id.acceptButton)
        acceptButton.setOnClickListener {
            finish()
        }

        mapView = findViewById(R.id.map)
        mapView.map.move(
            CameraPosition(
                Point(50.00000, 17.00000),/* zoom = */ 4.0f,/* azimuth = */ 0.0f,/* tilt = */ 0.0f
            )
        )

        dbHelper = DatabaseHelper(this)

        val allServers = dbHelper.getAllData()

        for (server in allServers) {
            server["latitude"]?.let {
                server["longitude"]?.let { it1 ->
                    setPin(
                        it.toDouble(), it1.toDouble()
                    )
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
//        dbHelper.close()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }



    private fun setPin(latitude: Double, longitude: Double) {
        val imageProvider = ImageProvider.fromResource(this, R.drawable.map_pin)
        val placeMark = mapView.map.mapObjects.addPlacemark().apply {
            geometry = Point(latitude, longitude)
            setIcon(imageProvider)
        }
        placeMark.setIconStyle(
            IconStyle().apply {
                anchor = PointF(0.5f, 1.0f)
                scale = 0.5f
            })

        placeMark.addTapListener(placeMarkTapListener)

//        placeMark.setText(
//            "VPN"
//        )
    }

    private val placeMarkTapListener = MapObjectTapListener { mapObject, _ ->
        if (mapObject is PlacemarkMapObject) {
            if (this::selectedPlaceMark.isInitialized) {
                selectedPlaceMark.setIcon(ImageProvider.fromResource(this, R.drawable.map_pin))
                selectedPlaceMark.setIconStyle(
                    IconStyle().apply {
                        anchor = PointF(0.5f, 1.0f)
                        scale = 0.5f
                    }
                )
            }
            mapObject.setIcon(ImageProvider.fromResource(this, R.drawable.map_pin_pressed))
            mapObject.setIconStyle(
                IconStyle().apply {
                    anchor = PointF(0.5f, 1.0f)
                    scale = 0.5f
                })
            changeFieldsSelectedServer(mapObject.geometry.latitude, mapObject.geometry.longitude)
            selectedPlaceMark = mapObject
        }
        true
    }

    private fun changeFieldsSelectedServer(latitude: Double, longitude: Double) {
        val id = dbHelper.getIdByCoordinates(latitude, longitude)
        findViewById<TextView>(R.id.country1).text = latitude.toString()
        findViewById<TextView>(R.id.city1).text = dbHelper.getDataById(2)!!.latitude.toString()
        if (id !== null) {
            val vpnServer = dbHelper.getDataById(id)
            if (vpnServer != null){
                findViewById<TextView>(R.id.country1).text = vpnServer.country
                findViewById<TextView>(R.id.city1).text = vpnServer.city
                findViewById<ImageView>(R.id.flag1).setImageResource(vpnServer.flagResourceId)
            }
        }
    }

}