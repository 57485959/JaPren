package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class RekapPresensiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekap_presensi)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        tabLayout.getTabAt(1)?.select()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    startActivity(Intent(this@RekapPresensiActivity, PresensiActivity::class.java))
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // RecyclerView Matkul (DUMMY)
        val rvMatkul = findViewById<RecyclerView>(R.id.rvMatkul)
        rvMatkul.layoutManager = LinearLayoutManager(this)

        val dummyMatkul = listOf(
            Matkul("Dasar Pemrograman", "Supriyanto, S.T., M.T.", 3, "A"),
            Matkul("Logika Informatika", "Ir. Nur Rochmah Dyah PA, S.T., M.Kom.", 3, "A")
        )

        rvMatkul.adapter = MatkulAdapter(dummyMatkul)
    }
}
