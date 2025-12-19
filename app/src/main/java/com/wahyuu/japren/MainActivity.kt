package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, RiwayatPresensiActivity::class.java))
                    true
                }

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
        val adapter = PresensiAdapter(listRiwayat)
        rvRiwayat.adapter = adapter

        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listRiwayat.clear()
                for (data in snapshot.children) {
                    val presensi = data.getValue(Presensi::class.java)
                    if (presensi != null) {
                        listRiwayat.add(presensi)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
}