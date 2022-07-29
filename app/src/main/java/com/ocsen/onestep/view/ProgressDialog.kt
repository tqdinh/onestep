package com.ocsen.onestep.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ocsen.onestep.R
import com.ocsen.onestep.databinding.DialogLoadingBinding

/**
 *  loading dialog
 */
class ProgressDialog(val context: Context, val title: String? = null) {

    private var dialog: Dialog? = null
    private var showing = false

    fun showDialog() {
        if (!showing) {
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.setCancelable(false)
            val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
            dialog?.setContentView(R.layout.dialog_loading)
            if (!title.isNullOrEmpty()) {
                binding.txtLoading.text = title
            }
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
            showing = true
        }
    }

    fun dismiss() {
        dialog?.dismiss()
        showing = false
    }

    fun isShowing(): Boolean {
        return dialog?.isShowing ?: false
    }
}
