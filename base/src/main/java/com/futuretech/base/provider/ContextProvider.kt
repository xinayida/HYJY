package com.futuretech.base.provider

import android.app.Application

object ContextProvider {

    lateinit var context: Application
        private set

    fun init(application: Application) {
        context = application
    }

}