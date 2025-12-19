package com.wahyuu.japren

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RiwayatPresensiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val presensi = data.getValue(Presensi::class.java)
                    // TAMPILKAN KE RECYCLERVIEW
                    Log.d("PRESENSI", presensi.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@RiwayatPresensiActivity,
                    "Gagal ambil data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}