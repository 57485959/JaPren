package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val txtGreeting = findViewById<TextView>(R.id.txtGreeting)

        val prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE)
        val nama = prefs.getString("NAMA", "Pengguna")

        txtGreeting.text = "Halo, $nama"

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> true // sudah di halaman ini

                R.id.menu_presensi -> {
                    startActivity(Intent(this, PresensiActivity::class.java))
                    true
                }

                R.id.menu_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        val rvRiwayat = findViewById<RecyclerView>(R.id.rvRiwayat)
        rvRiwayat.layoutManager = LinearLayoutManager(this)

        val listRiwayat = mutableListOf<Presensi>()
        val adapter = PresensiAdapter(listRiwayat, true) // TRUE = item_riwayat.xml
        rvRiwayat.adapter = adapter

        val ref = FirebaseDatabase
            .getInstance("https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("presensi_log")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listRiwayat.clear()

                for (data in snapshot.children) {
                    data.getValue(Presensi::class.java)?.let {
                        listRiwayat.add(it)
                    }
                }

                // Riwayat terbaru di paling atas
                listRiwayat.sortByDescending { it.timestamp ?: 0L }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // optional: log error
            }
        })
    }
}
