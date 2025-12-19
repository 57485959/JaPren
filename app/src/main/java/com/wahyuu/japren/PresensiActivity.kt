package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

class PresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presensi)

        // Toolbar back
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // tombol presensi -> MAPS
        val btnPresensi = findViewById<MaterialButton>(R.id.btnPresensi)
        btnPresensi.setOnClickListener {
            startActivity(Intent(this, MapsPresensiActivity::class.java))
        }
    }
}