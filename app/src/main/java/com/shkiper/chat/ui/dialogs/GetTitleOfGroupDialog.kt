package com.shkiper.chat.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.shkiper.chat.R


class GetTitleOfGroupDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater ?: throw KotlinNullPointerException()
        val view: View = inflater.inflate(R.layout.dialog_layout, null)

        builder.setView(view)
                .setTitle("Создание группы")
                .setPositiveButton("Ok") { _, _
                    -> TODO("Not yet implemented")
                }
                .setNegativeButton("Cancel"){ _, _
                        -> dismiss()
                }

        return builder.create()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_layout, container, false)
    }
}