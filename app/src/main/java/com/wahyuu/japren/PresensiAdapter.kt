package com.wahyuu.japren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PresensiAdapter (
    private  val list: List<Presensi>,
    private val isRiwayatPage: Boolean = false
    ) : RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // untuk item_riwayat.xml
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val txtWaktu: TextView = view.findViewById(R.id.txtWaktu)

        // untuk item_presensi.xml
        val tvStatusItem: TextView? = view.findViewById(R.id.tvStatusItem)
        val tvTimeItem: TextView? = view.findViewById(R.id.tvTimeItem)
        val tvDateItem: TextView? = view.findViewById(R.id.tvDateItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Pilih layout berdasarkan halaman
        val layout = if (isRiwayatPage) {
            R.layout.item_riwayat
        } else {
            R.layout.item_presensi
        }

        val view = LayoutInflater.from(parent.context)
            .inflate(layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        val safeTimestamp = data.timestamp ?: 0L
        val time = Date(safeTimestamp)

        if (isRiwayatPage) {
            // RIWAYAT
            holder.txtStatus?.text = data.status ?: "-"
            holder.txtWaktu?.text =
                SimpleDateFormat("dd MMM yyyy HH:mm", Locale("id", "ID"))
                    .format(time)
        } else {
            // PRESENSI
            holder.tvStatusItem?.text = data.status ?: "-"
            holder.tvTimeItem?.text =
                SimpleDateFormat("HH:mm 'WIB'", Locale("id", "ID"))
                    .format(time)
            holder.tvDateItem?.text =
                SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
                    .format(time)
        }
    }

    override fun getItemCount(): Int = list.size
}
