package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*

class PresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presensi)

        // Toolbar back
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // RecyclerView
        val rvPresensi = findViewById<RecyclerView>(R.id.rvPresensi)
        rvPresensi.layoutManager = LinearLayoutManager(this)

        val listPresensi = mutableListOf<Presensi>()
        val adapter = PresensiAdapter(listPresensi)
        rvPresensi.adapter = adapter

        // Firebase
        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        val nimLogin = getSharedPreferences("USER_PREF", MODE_PRIVATE).getString("NIM", "")

        ref.orderByChild("userId").equalTo(nimLogin)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    listPresensi.clear()
                    for (data in snapshot.children) {
                        val presensi = data.getValue(Presensi::class.java)
                        if (presensi != null) {
                            listPresensi.add(presensi)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@PresensiActivity,
                        "Gagal ambil data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        // Tambahkan ini di bawah inisialisasi TabLayout di PresensiActivity.kt
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 1) { // Jika klik tab 'Rekap'
                    val intent = Intent(this@PresensiActivity, RekapPresensiActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0) // Transisi mulus
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        val btnPresensi = findViewById<MaterialButton>(R.id.btnPresensi)
        btnPresensi.setOnClickListener {
            val daftarMatkul = arrayOf("Dasar Pemrograman", "Logika Informatika")

            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Pilih Mata Kuliah Hari Ini")
            builder.setItems(daftarMatkul) { _, which ->
                val matkulTerpilih = daftarMatkul[which]

                val intent = Intent(this, MapsPresensiActivity::class.java)
                intent.putExtra("EXTRA_MATKUL", matkulTerpilih)
                startActivity(intent)
            }
            builder.show()
        }
    }
}
