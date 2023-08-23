package com.futuretech.hyjl.recognition

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import java.util.Locale

interface ASRResultListener {
    fun onPartialResult(result: String)

    fun onFinalResult(result: String)
}

class RecognitionHelper(private val context: Context) : RecognitionListener {

    private val TAG = "Stefan"
    private lateinit var recognizer: SpeechRecognizer
    private lateinit var mResultListener: ASRResultListener

    fun prepareRecognition(resultListener: ASRResultListener): Boolean {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.e(TAG, "System has no recognition service yet.")
            return false
        }
        mResultListener = resultListener
        recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer.setRecognitionListener(this)
        return true
    }

    private fun createRecognitionIntent() = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        val language = Locale.getDefault()
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        Log.d(TAG, "createRecognitionIntent: $language")
    }

    fun startRecognition() {
        val intent = createRecognitionIntent()
        recognizer.startListening(intent)
    }

    fun stopRecognition() {
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.e(TAG, "onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Log.e(TAG, "onBeginningOfSpeech")
    }

    override fun onRmsChanged(p0: Float) {
        Log.e(TAG, "onRmsChanged $p0")
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onEndOfSpeech() {
        Log.e(TAG, "onEndOfSpeech")
    }

    override fun onError(errorCode: Int) {
        Log.e(TAG, "Recognition onError $errorCode")
    }

    override fun onResults(bundle: Bundle?) {
        bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
            Log.d(TAG, "onResults() with:$bundle results:$it")

            mResultListener.onFinalResult(it[0])
        }
    }


    override fun onPartialResults(bundle: Bundle?) {
        bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let {
            Log.d(
                TAG, "onPartialResults() with:$bundle results:$it"
            )

            mResultListener.onPartialResult(it[0])
        }

    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        Log.e(TAG, "Recognition onEvent $p0 $p1")
    }

}