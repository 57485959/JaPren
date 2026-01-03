package com.wahyuu.japren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PresensiHarianAdapter(
    private val list: List<Presensi>,
    private val namaDosen: String
) : RecyclerView.Adapter<PresensiHarianAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPertemuan: TextView = view.findViewById(R.id.tvPertemuan)
        val tvTanggal: TextView = view.findViewById(R.id.tvTanggal)
        val tvDosen: TextView = view.findViewById(R.id.tvDosen)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_presensi_harian, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        val sdfTanggal = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
        val sdfWaktu = SimpleDateFormat("HH:mm", Locale("id", "ID"))
        val dateObj = Date(data.timestamp ?: 0L)

        holder.tvPertemuan.text = "${position + 1}"
        holder.tvTanggal.text = "${sdfTanggal.format(dateObj)} (${sdfWaktu.format(dateObj)} WIB)"
        holder.tvDosen.text = namaDosen

        holder.tvStatus.text = data.status

        if (data.status == "Hadir") {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.hijau_tua))
        } else {
            holder.tvStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
        }
    }

    override fun getItemCount(): Int = list.size
}