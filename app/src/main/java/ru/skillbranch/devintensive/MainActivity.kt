package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.models.Bender


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

        messageEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
                messageEt.setText("")
                val (r,g,b) = color
                benderImage.setColorFilter(Color.rgb(r,b,g),PorterDuff.Mode.MULTIPLY)
                textTxt.text = phrase
                hideKeyboard()
                true
            } else {
                false
            }
        }



        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,b,g),PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()

        sendBtn.setOnClickListener(this)

        Log.d("M_MainActivity","onCreate")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.question)
        Log.d("M_MainActivity","OnSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }


    override fun onClick(v: View?) {
        if(v?.id == R.id.iv_send){
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
            messageEt.setText("")
            val (r,g,b) = color
            benderImage.setColorFilter(Color.rgb(r,b,g),PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
            hideKeyboard()
        }
    }
}
