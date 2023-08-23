package com.futuretech.hyjl
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class OriSpeech2Text : Activity() {
    private var btnSpeak: ImageButton? = null
    private var txtText: TextView? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ori_speech2text)
        txtText = findViewById<View>(R.id.txtText) as TextView
        btnSpeak = findViewById<View>(R.id.btnSpeak) as ImageButton
        btnSpeak!!.setOnClickListener {
            val intent = Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            //提示语言开始文字，就是效果图上面的文字
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please start your voice");
            try {
                startActivityForResult(intent, RESULT_SPEECH)
                txtText!!.text = ""
            } catch (a: ActivityNotFoundException) {
                val t = Toast.makeText(
                    applicationContext,
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT
                )
                t.show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RESULT_SPEECH -> {
                if (resultCode == RESULT_OK) {
                    val text = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    txtText!!.text = text!![0]
                }
            }
        }
    }

    companion object {
        protected const val RESULT_SPEECH = 1
    }
}