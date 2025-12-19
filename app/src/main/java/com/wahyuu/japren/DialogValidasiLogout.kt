package com.wahyuu.japren

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment

class DialogValidasiLogout : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.validasi_logout)

        // Membuat background dialog asli menjadi transparan agar rounded corner di XML terlihat
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnAgree = dialog.findViewById<Button>(R.id.btnAgreeLO)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelLO)

        btnAgree.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            dismiss()
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        if (window != null) {
            val params = window.attributes

            // 1. Mengatur posisi di bawah (Bottom)
            params.gravity = Gravity.BOTTOM

            // 2. Mengatur lebar full screen (agar menempel ke pinggir jika diinginkan)
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT

            // 3. Mengatur kegelapan background (0.0 sampai 1.0)
            // Semakin tinggi nilainya, semakin gelap/redup
            params.dimAmount = 0.7f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            window.attributes = params

            // Opsional: Animasi muncul dari bawah
            // window.windowAnimations = android.R.style.Animation_InputMethod
        }
    }
}