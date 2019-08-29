package com.joeys.expriment

import android.Manifest
import android.app.KeyguardManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.webkit.*
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.joeys.expriment.fragments.MainFragment
import com.joeys.expriment.fragments.SecondFragment
import com.joeys.router.annotation.Builder
import com.joeys.router.annotation.Required
import androidx.lifecycle.ViewModelProviders
import com.alibaba.fastjson.JSON
import com.joeys.expriment.utils.log
import com.joeys.router.annotation.Optional
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.lang.StringBuilder
import java.util.*

import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var mViewpager: ViewPager
    private lateinit var mMessage: TextView
    private lateinit var mBtnToEmotion: Button
    private lateinit var mWebview: WebView
    private lateinit var mNavView: BottomNavigationView
    private lateinit var mContainer: ConstraintLayout


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    lateinit var userName: String

    var userAge: Int = 0

    var isChildern: Boolean = false


    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


        mViewpager = findViewById<ViewPager>(R.id.viewpager)
        mMessage = findViewById<TextView>(R.id.message)
        mBtnToEmotion = findViewById<Button>(R.id.btn_to_emotion)
        mWebview = findViewById<WebView>(R.id.webview)
        mNavView = findViewById<BottomNavigationView>(R.id.nav_view)
        mContainer = findViewById<ConstraintLayout>(R.id.container)
        val d = RxPermissions(this)
                .request(Manifest.permission.PACKAGE_USAGE_STATS)
                .subscribe { getAppUsage() }
//
//
//        seWebSettings(mWebview)
//        mWebview.loadUrl("https://map.baidu.com/")

        deviceTest()
    }


    fun deviceTest() {


        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager


        val hasPwd = keyguardManager.isDeviceSecure
        "hasPwd $hasPwd".log()
        val screenBrigntess = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        "screenBrigntess $screenBrigntess".log()

        val enableAdb = Settings.Secure.getInt(contentResolver, Settings.Global.ADB_ENABLED)
        "enableAdb $enableAdb".log()
        Toast.makeText(this, "enableAdb $enableAdb", Toast.LENGTH_LONG).show()

        val cpu = getCpuName()
        "cpu info ${cpu}".log()

        val memoryTotal = aa.getTotalMemory(this)
        "memoryTotal   ${memoryTotal}".log()
        getAppInfo()

//        val intent = Intent()
//        intent.setAction(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//        startActivity(intent)

    }

    val useTimeDataManager = UseTimeDataManager.getInstance(this)
    fun getAppUsage() {
        useTimeDataManager.refreshData(0)
        val str = getJsonObjectStr()
        "AppUsage $str".log()
    }

    fun getAppInfo() {


        var packageInfos = packageManager.getInstalledPackages(0)

        val mLocalInstallApps = mutableListOf<AppInfo>()
        for (packageInfo in packageInfos) {
            if (ApplicationInfo.FLAG_SYSTEM and packageInfo.applicationInfo.flags != 0)
                continue
            packageInfo.lastUpdateTime
            mLocalInstallApps.add(
                    AppInfo(
                            packageInfo.applicationInfo.loadLabel(packageManager).toString(),
                            packageInfo.packageName
                    ).apply {
                        installTime = DateTransUtils.stampToDate(packageInfo.firstInstallTime)
                        lastUpdateTime = DateTransUtils.stampToDate(packageInfo.lastUpdateTime)
                    }
            )
        }

        val json = JSON.toJSONString(mLocalInstallApps)
//        "app info $json".log()
    }

    fun getJsonObjectStr(): String {
        var jsonAppdeTails = ""
        try {
            val packageInfos = useTimeDataManager.getmPackageInfoListOrderByTime()
            val jsonObject2 = JSONObject()
            val jsonArray = JSONArray()
            for (i in packageInfos.indices) {
                try {
                    val jsonObject = JSONObject()
                    jsonArray.put(i, jsonObject.accumulate("count", packageInfos.get(i).getmUsedCount()))
                    jsonArray.put(i, jsonObject.accumulate("name", packageInfos.get(i).getmPackageName()))
                    jsonArray.put(i, jsonObject.accumulate("time", packageInfos.get(i).getmUsedTime()))
                    jsonArray.put(i, jsonObject.accumulate("appname", packageInfos.get(i).getmAppName()))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return ""
                }

            }
            jsonObject2.put("details", jsonArray)
            jsonAppdeTails = jsonObject2.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
            return ""
        }

        return jsonAppdeTails
    }

    fun getCpuName(): String? {
        try {
            val fr = FileReader("/proc/cpuinfo")
            val br = BufferedReader(fr)
            val text = br.readLine()
            val array = text.split(":\\s+".toRegex(), 2).toTypedArray()
            for (i in array.indices) {
            }
            return array[1]
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    fun getCPUInfo(): Map<String, String> {

        val br = BufferedReader(FileReader("/proc/cpuinfo"));

        val sb = StringBuilder()
        var str = "";

        var output = HashMap<String, String>()

        while (true) {
            str = br.readLine()
            if (str.isNullOrEmpty())
                break

            sb.append(str)

            var data = str.split(":")

            if (data.size > 1) {

                var key = data[0].trim().replace(" ", "_");
                if (key.equals("model_name")) key = "cpu_model";

                var value = data[1].trim();

                if (key.equals("cpu_model"))
                    value = value.replace("\\s+", " ");

                output.put(key, value)

            }

        }

        br.close()
        "cpu info ${sb}".log()

        return output

    }

    fun seWebSettings(webView: WebView) {
        val webseting = webView.settings

        webseting.defaultTextEncodingName = "UTF-8"
        webseting.allowFileAccess = false
        webseting.setAppCacheEnabled(true)
        webseting.allowContentAccess = true
        webseting.allowUniversalAccessFromFileURLs = true
        webseting.allowFileAccessFromFileURLs = true

        webseting.databaseEnabled = true
        webseting.setGeolocationEnabled(true)
        val dir = this.applicationContext.getDir("database", Context.MODE_PRIVATE).path
        webseting.setAppCacheEnabled(true)
        webseting.setAppCachePath(dir)

        //自适应屏幕
        webseting.cacheMode = WebSettings.LOAD_DEFAULT
        webseting.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        //        webseting.setRenderPriority(WebSettings.RenderPriority.HIGH);//提高渲染的优先级

        // 开启 DOM storage API 功能
        webseting.domStorageEnabled = true
        webseting.javaScriptEnabled = true
        // 设置可以支持缩放
        webseting.setSupportZoom(true)
        //扩大比例的缩放
        webseting.useWideViewPort = true
        webseting.loadWithOverviewMode = true
        webseting.blockNetworkImage = false//把图片加载放在最后来加载渲染
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webseting.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith("http") || url.startsWith("https") || url.startsWith("ftp")) {
                    view.loadUrl(url) //在当前的webview中跳转到新的url
                    //不处理http, https, ftp的请求
                } else {


                }
                return true
            }
        }

        webView.webChromeClient = object : WebChromeClient() {

            //显示获取地理位置提示对话框
            //此方法在Android.M及以上，并且访问的链接是https时才会调用，否则会调用系统原生dialog
            override fun onGeolocationPermissionsShowPrompt(origin: String, callback: GeolocationPermissions.Callback) {

                //展示自定义权限dialog
                val remember = false
                val builder = AlertDialog.Builder(this@MainActivity);
                builder.setTitle("位置信息")//自定义title
                builder.setMessage("允许刷宝短视频获取您的地理位置信息吗？")//自定义文案
                        .setCancelable(true)
                        .setPositiveButton("允许"
                        ) { _, _ ->
                            callback.invoke(origin, true, remember)
                        }
                        .setNegativeButton("不允许"
                        ) { _, _ ->
                            callback.invoke(origin, false, remember)
                        }
                builder.show()
            }
        }
    }

    class RequstTask<T> private constructor() {


        companion object {
            private val INSTANCE: RequstTask<Any>
                    by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED)
                    {
                        RequstTask<Any>()
                    }

            fun <T> requstQuene(request: Observable<T>) {
                RequstTask<T>()

            }

        }
    }

    class RequstManager<T> {

        private val isRequsting = AtomicBoolean(false)
        private val hasValue = AtomicBoolean(false)
        private val mWaitingPublisher = PublishSubject.create<T>()

        fun request(observable: Observable<T>): Observable<T> {

            if (!hasValue.get() && isRequsting.compareAndSet(false, true)) {
                return observable.doOnNext {
                    mWaitingPublisher.onNext(it)
                    isRequsting.set(false)
                }
            } else {
                return mWaitingPublisher
                        .doOnNext {
                            hasValue.set(true)
                        }
            }

        }

    }

    class AppInfo(appName: String, packageName: String) {
        var appName: String? = appName
        var packageName: String? = packageName
        var installTime: String = ""
        var lastUpdateTime: String = ""
    }

}
