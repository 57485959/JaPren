package com.wahyuu.japren

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class DetailRekapPresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_detail_rekap_presensi_matkul)

        // ==== AMBIL DATA MATKUL DARI INTENT ====
        val namaMatkul = intent.getStringExtra("nama") ?: "-"
        val kelas = intent.getStringExtra("kelas") ?: "-"
        val sks = intent.getIntExtra("sks", 0)

        findViewById<TextView>(R.id.tvNamaMatkul).text = namaMatkul
        findViewById<TextView>(R.id.tvInfoMatkul).text =
            "Kelas $kelas • $sks SKS"

        // ==== RECYCLERVIEW PRESENSI HARIAN ====
        val rv = findViewById<RecyclerView>(R.id.rv_pertemuan)
        rv.layoutManager = LinearLayoutManager(this)

        val listPresensi = mutableListOf<Presensi>()
        val adapter = PresensiHarianAdapter(listPresensi)
        rv.adapter = adapter

        // ==== FIREBASE ====
        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("presensi_log")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listPresensi.clear()

                var hadir = 0
                var absen = 0

                for (data in snapshot.children) {
                    val presensi = data.getValue(Presensi::class.java)
                    if (presensi != null) {
                        listPresensi.add(presensi)

                        if (presensi.status == "Hadir") hadir++
                        else absen++
                    }
                }

                // update rekap
                findViewById<TextView>(R.id.tvTotalHadir).text = hadir.toString()
                findViewById<TextView>(R.id.tvTotalAbsen).text = absen.toString()

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
