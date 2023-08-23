package com.futuretech.hyjl

import android.app.Application
import com.futuretech.base.provider.AppThemeProvider
import com.futuretech.base.provider.ContextProvider
import com.futuretech.base.provider.ImageLoaderProvider
import com.futuretech.base.provider.ToastProvider


const val TAG = "Stefan"
class MyApplication : Application() {

    override fun onCreate() {
//        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
//        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));

        AppThemeProvider.init(application = this)
        ImageLoaderProvider.init(application = this)
        ToastProvider.init(application = this)
        ContextProvider.init(application = this)
        super.onCreate()
    }
}