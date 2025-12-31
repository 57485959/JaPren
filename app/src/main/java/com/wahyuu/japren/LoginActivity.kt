package com.wahyuu.japren

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtUsername = findViewById<TextInputEditText>(R.id.edtUsername)
        val edtPassword = findViewById<TextInputEditText>(R.id.edtPassword)
        val btnMasuk = findViewById<MaterialButton>(R.id.btnMasuk)

        val database = FirebaseDatabase.getInstance(
            "https://japren-749fa-default-rtdb.asia-southeast1.firebasedatabase.app/"
        )
        val ref = database.getReference("users")


        btnMasuk.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Username dan password wajib diisi!!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                ref.child(username)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                val dbPassword =
                                    snapshot.child("password").value.toString()

                                if (dbPassword == password) {
                                    // LOGIN BERHASIL
                                    val nama = snapshot.child("nama").value.toString()
                                    val nim = snapshot.child("nim").value.toString()
                                    val prodi = snapshot.child("prodi").value.toString()

                                    val prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE)
                                    val editor = prefs.edit()
                                    editor.putString("NIM", nim)
                                    editor.putString("NAMA", nama)
                                    editor.putString("PRODI", prodi)
                                    editor.apply()

                                    startActivity(
                                        Intent(this@LoginActivity, MainActivity::class.java)
                                    )
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Password salah",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Username tidak ditemukan",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Gagal login",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }
}
