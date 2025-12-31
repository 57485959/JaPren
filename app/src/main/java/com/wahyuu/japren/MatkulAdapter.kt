package com.wahyuu.japren

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MatkulAdapter(
    private val listMatkul: List<Matkul>
) : RecyclerView.Adapter<MatkulAdapter.MatkulViewHolder>() {

    inner class MatkulViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNamaMatkul: TextView = itemView.findViewById(R.id.tvNamaMatkul)
        val tvNamaDosen: TextView = itemView.findViewById(R.id.tvNamaDosen)
        val tvSks: TextView = itemView.findViewById(R.id.tvSks)
        val tvKelas: TextView = itemView.findViewById(R.id.tvKelas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatkulViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_matkul, parent, false)
        return MatkulViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatkulViewHolder, position: Int) {
        val matkul = listMatkul[position]

        holder.tvNamaMatkul.text = matkul.namaMatkul
        holder.tvNamaDosen.text = matkul.namaDosen
        holder.tvSks.text = matkul.sks.toString()
        holder.tvKelas.text = matkul.kelas
    }

    override fun getItemCount(): Int = listMatkul.size
}
