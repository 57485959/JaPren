package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val txtName = findViewById<TextView>(R.id.txtName)
        val valueNama = findViewById<TextView>(R.id.valueNama)
        val valueNim = findViewById<TextView>(R.id.valueNim)

        // ambil data user
        val prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val nama = prefs.getString("NAMA", "Nama Mahasiswa")
        val nim = prefs.getString("NIM", "-")

        txtName.text = nama
        valueNama.text = nama
        valueNim.text = nim

        val btnKeluar = findViewById<MaterialButton>(R.id.btnKeluar)
        btnKeluar.setOnClickListener {
            val dialog = DialogValidasiLogout()
            dialog.show(supportFragmentManager, "DialogLogout")
        }

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.selectedItemId = R.id.menu_profile

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.menu_presensi -> {
                    startActivity(Intent(this, PresensiActivity::class.java))
                    true
                }
                R.id.menu_profile -> true
                else -> false
            }
        }
    }
}