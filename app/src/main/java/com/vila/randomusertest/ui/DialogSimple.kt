package com.vila.randomusertest.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.vila.randomusertest.R
import com.vila.randomusertest.databinding.FragmentDialogSimpleBinding

class DialogSimple : DialogFragment() {
    private lateinit var binding: FragmentDialogSimpleBinding
    private var listener: OnDialogSimpleListener? = null

    companion object {
        fun newInstance(tittle: String, message: String) =
            DialogSimple().apply {
                arguments = Bundle()
                    .apply {
                        putString("titulo", tittle)
                        putString("message", message)
                    }
            }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            // window?.setDimAmount(0f)
            window?.setBackgroundDrawable(
                ContextCompat
                    .getDrawable(context, (R.drawable.fondo_dialogo_simple))
            )
            window?.attributes?.windowAnimations = R.style.Dialog_animations
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogSimpleBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        binding.mensaje.text = arguments?.getString("message")
        binding.titulo.text = arguments?.getString("tittle")

        binding.btOK.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener?.buttonClick()
                dismiss()
            }
        })
    }

    fun setOnDialogSimpleListener(listener: OnDialogSimpleListener) {
        this.listener = listener
    }

     interface OnDialogSimpleListener {
        fun buttonClick()
    }

}