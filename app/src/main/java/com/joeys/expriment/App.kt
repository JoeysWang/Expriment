package com.joeys.expriment

import android.app.Application
import android.content.Context
import com.bun.miitmdid.core.JLibrary
import com.bun.miitmdid.core.MdidSdkHelper
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.bun.miitmdid.core.IIdentifierListener
import com.joeys.expriment.utils.DensityUtils
import com.joeys.expriment.utils.log


class App : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        "attachBaseContext".log()
        JLibrary.InitEntry(base)
    }

}