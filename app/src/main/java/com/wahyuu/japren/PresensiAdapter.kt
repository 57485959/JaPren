package com.wahyuu.japren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PresensiAdapter (private  val list: List<Presensi>) : RecyclerView.Adapter<PresensiAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtStatus: TextView = view.findViewById(R.id.txtStatus)
        val txtWaktu: TextView = view.findViewById(R.id.txtWaktu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_riwayat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        holder.txtStatus.text = data.status

        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
        holder.txtWaktu.text = sdf.format(Date(data.timestamp ?: 0))
    }

    override fun getItemCount(): Int = list.size
}
