package com.example.vpn

import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yandex.mapkit.mapview.MapView


class MainActivity2 : AppCompatActivity() {

    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        MapKitFactory.initialize(this)

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


//        MapKitFactory.setApiKey("f2e06fdb-9688-4224-9a52-83ccb2f715c2")
//        mapView = findViewById(R.id.map)

//        mapView.onStart();
//        MapKitFactory.getInstance().onStart();

        val acceptButton: ImageButton = findViewById(R.id.acceptButton)
        acceptButton.setOnClickListener {
            finish()
        }
    }

//    override fun onStart() {
//        super.onStart()
//        MapKitFactory.getInstance().onStart()
//        mapView.onStart()
//    }
//
//    override fun onStop() {
//        mapView.onStop()
//        MapKitFactory.getInstance().onStop()
//        super.onStop()
//    }
}