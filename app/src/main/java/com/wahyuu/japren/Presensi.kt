package com.wahyuu.japren

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties // Menghindari crash jika ada field tambahan di Firebase

data class Presensi(
    var userId: String? = "",
    var status: String? = "",
    var timestamp: Long? = 0L,
    var date: String? = "",
    var time: String? = "" // Tambahkan ini karena kita tadi menambah field time
){
    // Constructor kosong otomatis dibuat oleh Kotlin jika semua field punya nilai default
}
