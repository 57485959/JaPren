package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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