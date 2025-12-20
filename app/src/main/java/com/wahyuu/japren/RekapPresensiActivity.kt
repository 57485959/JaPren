package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*

class RekapPresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekap_presensi)

        // 1. Toolbar Back (Sama seperti PresensiActivity)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // 2. Logika Tab Layout
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        // Set tab 'Rekap' (indeks 1) sebagai yang terpilih saat masuk
        tabLayout.getTabAt(1)?.select()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    // Jika klik tab Presensi GPS (indeks 0), balik ke activity awal
                    val intent = Intent(this@RekapPresensiActivity, PresensiActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) // Agar tidak tumpuk-tumpuk
                    startActivity(intent)

                    // Animasi 0,0 supaya bagian header (Toolbar/Tab) tidak terlihat gerak
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // 3. Firebase (Opsional: Jika kamu ingin mengisi data statistik secara dinamis)
        setupFirebaseData()
    }

    private fun setupFirebaseData() {
        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        val tvTotalHadir = findViewById<TextView>(R.id.tvTotalHadir)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val totalHadir = snapshot.childrenCount // Menghitung jumlah baris data
                    tvTotalHadir.text = totalHadir.toString()
                } else {
                    tvTotalHadir.text = "0"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RekapPresensiActivity , "Gagal sinkron data rekap", Toast.LENGTH_SHORT).show()
            }
        })
    }
}