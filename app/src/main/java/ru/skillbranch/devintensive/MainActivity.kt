package ru.skillbranch.devintensive

import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardClosed
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    lateinit var benderImage: ImageView
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView

    lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = findViewById(R.id.iv_bender)
        textTxt = findViewById(R.id.tv_text)
        messageEt = findViewById(R.id.et_message)
        sendBtn = findViewById(R.id.iv_send)

        sendBtn.setOnClickListener(this)
        messageEt.setOnEditorActionListener(this)

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        val (r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        Log.i("DEVINTENSIVE", "onCreate ${this.isKeyboardOpen()} ${this.isKeyboardClosed()}")
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.iv_send){
            sendMsg()
        }
    }

    override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
        sendMsg()
        return true
    }

    private fun sendMsg(){
        Log.i("DEVINTENSIVE","${this.isKeyboardOpen()} ${this.isKeyboardClosed()}")
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
        messageEt.setText("")
        val (r,g,b) = color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase

        this.hideKeyboard()
        Log.i("DEVINTENSIVE","${this.isKeyboardOpen()} ${this.isKeyboardClosed()}")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
    }
}
