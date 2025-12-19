package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.FirebaseDatabase

class PresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presensi)

        val database = FirebaseDatabase.getInstance("https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/")
        val myRef = database.getReference("presensi_log")

        // Toolbar back
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // tombol presensi -> MAPS
        val btnPresensi = findViewById<MaterialButton>(R.id.btnPresensi)
        btnPresensi.setOnClickListener {
            startActivity(Intent(this, MapsPresensiActivity::class.java))
            val presensiData = mapOf(
                "userId" to "user_wahyu",
                "status" to "Hadir",
                "timestamp" to System.currentTimeMillis()
            )

            myRef.push().setValue(presensiData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data tersimpan di Firebase!", Toast.LENGTH_SHORT).show()
                    // Setelah data tersimpan, baru pindah ke Maps
                    startActivity(Intent(this, MapsPresensiActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal simpan data: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}