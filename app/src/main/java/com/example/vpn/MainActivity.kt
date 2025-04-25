package com.example.vpn

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var isON: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainConstraint)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<View>(R.id.pop_up_back).visibility = View.GONE

        findViewById<LinearLayout>(R.id.defaultServer).setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.mainButton).setOnClickListener { l -> clickVPNButton(l) }
        findViewById<Button>(R.id.cancel_butt).setOnClickListener { cancelDisconnect() }
        findViewById<Button>(R.id.disconnect_butt).setOnClickListener { disconnect() }
        findViewById<ImageButton>(R.id.close_butt).setOnClickListener { cancelDisconnect() }
    }

    private fun clickVPNButton(mainButton: View) {
        mainButton as ImageButton

        checkIpByApi()

        if (!isON) {
            mainButton.setImageResource(R.drawable.button_load)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                mainButton.setImageResource(R.drawable.button_off)
                findViewById<TextView>(R.id.state).text = "00 : 00 : 01"
//                findViewById<TextView>(R.id.ip).text = "Ваш IP: 51.77.108.59"
                findViewById<TextView>(R.id.country).text = "Великобритания"
                findViewById<TextView>(R.id.city).text = "Лондон"
                findViewById<ImageView>(R.id.flag).setImageResource(R.drawable.flag_gb)
                isON = true
            }, 500)
        } else {
            findViewById<View>(R.id.pop_up_back).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.pop_up).visibility = View.VISIBLE
        }
    }

    private fun cancelDisconnect() {
        findViewById<View>(R.id.pop_up_back).visibility = View.GONE
        findViewById<ConstraintLayout>(R.id.pop_up).visibility = View.GONE
    }

    private fun disconnect() {
        findViewById<View>(R.id.pop_up_back).visibility = View.GONE
        findViewById<ConstraintLayout>(R.id.pop_up).visibility = View.GONE

        findViewById<ImageButton>(R.id.mainButton).setImageResource(R.drawable.button_on)
        findViewById<TextView>(R.id.state).setText(R.string.disable_state)
        findViewById<TextView>(R.id.ip).setText(R.string.default_ip)
        findViewById<TextView>(R.id.country).setText(R.string.default_country)
        findViewById<TextView>(R.id.city).setText(R.string.default_city)
        findViewById<ImageView>(R.id.flag).setImageResource(R.drawable.flag_blank)
        isON = false
    }

    private fun checkIpByApi() {
        val ipTextField = findViewById<TextView>(R.id.ip)
        lifecycleScope.launch {
            val ipFromApi = Api.getIpAddress()
            if (ipFromApi != null) {
                ipTextField.text = "Ваш IP: "+ ipFromApi
            } else {
                ipTextField.text = "Api don't know"
            }
        }

    }


}