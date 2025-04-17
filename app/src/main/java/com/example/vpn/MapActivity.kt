package com.example.vpn

import android.graphics.PointF
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider


class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var dbHelper: DatabaseHelper
    private var mapIsOpen: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!mapIsOpen) {
            MapKitFactory.setApiKey("f2e06fdb-9688-4224-9a52-83ccb2f715c2")
            MapKitFactory.initialize(this)
            mapIsOpen = true
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
                Point(50.00000, 17.00000),
                /* zoom = */ 4.0f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 0.0f
            )
        )

        dbHelper = DatabaseHelper(this)

        val allServers = dbHelper.getAllData()

        for (server in allServers) {
            server["latitude"]?.let {
                server["longitude"]?.let { it1 ->
                    setPin(
                        it.toDouble(),
                        it1.toDouble()
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
            }
        )
    }

}