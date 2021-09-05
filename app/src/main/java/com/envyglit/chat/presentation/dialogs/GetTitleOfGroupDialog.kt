package com.envyglit.chat.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.envyglit.chat.R

class GetTitleOfGroupDialog : DialogFragment() {

    private var editTextTitleGroup: EditText? = null
    private lateinit var dialogListener: GetTitleDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater ?: throw KotlinNullPointerException()
        val view: View = inflater.inflate(R.layout.dialog_layout, null)

        builder.setView(view)
                .setTitle("Создание группы")
                .setPositiveButton("Ok") { _, _
                    -> dialogListener.getTitleOfChat(editTextTitleGroup!!.text.toString())
                }
                .setNegativeButton("Cancel"){ _, _
                        -> dismiss()
                }
        editTextTitleGroup = view.findViewById(R.id.edit_title_of_group);

        return builder.create()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_layout, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener =  context as GetTitleDialogListener
        }
        catch (e: ClassCastException){
            throw ClassCastException(
                context.toString() +
                        "must implement DialogListener"
            );
        }
    }

    interface GetTitleDialogListener {
        fun getTitleOfChat(titleOfChat: String)
    }
}