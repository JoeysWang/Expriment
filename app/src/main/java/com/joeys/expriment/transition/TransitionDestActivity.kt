package com.joeys.expriment.transition

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Pair
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.NestedScrollView
import androidx.palette.graphics.Palette
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.joeys.draglayout.ElasticDragDismissFrameLayout
import com.joeys.expriment.R
import com.bumptech.glide.request.target.Target
import com.joeys.draglayout.AnimUtils.getFastOutSlowInInterpolator
import com.joeys.draglayout.ColorUtils
import com.joeys.draglayout.ViewUtils
import com.joeys.draglayout.extension.getBitmap
import com.joeys.expriment.utils.log
import kotlinx.coroutines.*


class TransitionDestActivity : AppCompatActivity() {
    private lateinit var shot: ImageView
    private lateinit var draggableFrame: ElasticDragDismissFrameLayout
    private lateinit var chromeFader: ElasticDragDismissFrameLayout.SystemChromeFader
    private lateinit var background: View
    private lateinit var bodyScroll: NestedScrollView
    private lateinit var back: ImageButton
    private lateinit var backWrapper: FrameLayout
    private lateinit var shotSpacer: View


    private val imageUrl: String by lazy { intent.getStringExtra("image") }


    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_dest)
        background = findViewById<View>(R.id.background)
        bodyScroll = findViewById<NestedScrollView>(R.id.body_scroll)
        back = findViewById<ImageButton>(R.id.back)
        backWrapper = findViewById<FrameLayout>(R.id.back_wrapper)
        draggableFrame = findViewById(R.id.comments_container)
        shotSpacer = findViewById<View>(R.id.shot_spacer)
        shot = findViewById(R.id.shot)

        chromeFader = object : ElasticDragDismissFrameLayout.SystemChromeFader(this) {
            override fun onDragDismissed() {
                finishAfterTransition()
            }
        }
        draggableFrame.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        draggableFrame.doOnApplyWindowInsets { view, insets, padding, margin ->
            val left = insets.systemWindowInsetLeft
            val top = insets.systemWindowInsetTop
            val right = insets.systemWindowInsetRight
            val bottom = insets.systemWindowInsetBottom

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = margin.left + left
                topMargin = margin.top + top
                rightMargin = margin.right + right
            }

        }
        bodyScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            //            shot.offset = -scrollY
        }

        val imgSizeList = findWithAndHeight(imageUrl)
        var imgW = imgSizeList[0]
        var imgH = imgSizeList[1]


        val ivWith = ScreenUtils.getScreenWidth()
        val ivHeight = calImgHeightByWith(imgW, imgH, ivWith)
        val lp = shot.layoutParams
        lp.height = ivHeight
        lp.width = ivWith
        "image lp $ivHeight".log()
        shot.layoutParams = lp
        shotSpacer.layoutParams.height=ivHeight

        Glide.with(this)
                .load(imageUrl)
                .into(shot)

        MainScope().launch {
            delay(1000)
            " screen:${ScreenUtils.getScreenHeight()} draggableFrame:${draggableFrame.measuredHeight}  shot.top${shot.top} back.top${backWrapper.top}".log()
        }

    }


    class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

    class InitialMargin(val left: Int, val top: Int, val right: Int, val bottom: Int)

    fun View.doOnApplyWindowInsets(block: (View, WindowInsets, InitialPadding, InitialMargin) -> Unit) {
        // Create a snapshot of the view's padding & margin states
        val initialPadding = recordInitialPaddingForView(this)
        val initialMargin = recordInitialMarginForView(this)
        // Set an actual OnApplyWindowInsetsListener which proxies to the given
        // lambda, also passing in the original padding & margin states
        setOnApplyWindowInsetsListener { v, insets ->
            block(v, insets, initialPadding, initialMargin)
            // Always return the insets, so that children can also use them
            insets
        }
        // request some insets
        requestApplyInsetsWhenAttached()
    }

    fun View.requestApplyInsetsWhenAttached() {
        if (isAttachedToWindow) {
            // We're already attached, just request as normal
            requestApplyInsets()
        } else {
            // We're not attached to the hierarchy, add a listener to
            // request when we are
            addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    v.removeOnAttachStateChangeListener(this)
                    v.requestApplyInsets()
                }

                override fun onViewDetachedFromWindow(v: View) = Unit
            })
        }
    }

    private fun recordInitialPaddingForView(view: View) = InitialPadding(
            view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
    )

    private fun recordInitialMarginForView(view: View): InitialMargin {
        val lp = view.layoutParams as? ViewGroup.MarginLayoutParams
                ?: throw IllegalArgumentException("Invalid view layout params")
        return InitialMargin(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin)
    }

    fun findWithAndHeight(imgUrl: String): List<Int> {
        val r = Regex("(\\d+x\\d+)\$").find(imgUrl)?.value
        if (r.isNullOrBlank()) {
            return arrayListOf(0, 0)
        }
        return r.split("x").map {
            it.toIntOrNull() ?: 0
        }
    }

    private val shotLoadListener = object : RequestListener<Drawable> {
        override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
        ): Boolean {
            val bitmap = resource.getBitmap() ?: return false

            Palette.from(bitmap)
                    .clearFilters() /* by default palette ignore certain hues
                        (e.g. pure black/white) but we don't want this. */
                    .generate { palette -> applyFullImagePalette(palette) }

            val twentyFourDip = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    24f,
                    this@TransitionDestActivity.resources.displayMetrics
            ).toInt()
            Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.width - 1, twentyFourDip)
                    .generate { palette -> applyTopPalette(bitmap, palette) }
            return false
        }

        override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable>,
                isFirstResource: Boolean
        ) = false
    }

    internal fun applyFullImagePalette(palette: Palette?) {
        // color the ripple on the image spacer (default is grey)
        shotSpacer.background = ViewUtils.createRipple(
                palette, 0.25f, 0.5f,
                ContextCompat.getColor(this, R.color.mid_grey), true
        )
//        shotSpacer.setBackgroundColor(Color.GREEN)
        // slightly more opaque ripple on the pinned image to compensate for the scrim
        shot.foreground = ViewUtils.createRipple(
                palette, 0.3f, 0.6f,
                ContextCompat.getColor(this, R.color.mid_grey), true
        )
    }

    internal fun applyTopPalette(bitmap: Bitmap, palette: Palette?) {
        val lightness = ColorUtils.isDark(palette)
        val isDark = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
            ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
        } else {
            lightness == ColorUtils.IS_DARK
        }

        if (!isDark) { // make back icon dark on light images
            back.setColorFilter(
                    ContextCompat.getColor(this, R.color.background_super_dark)
            )
        }

        // color the status bar.
        var statusBarColor = window.statusBarColor
        ColorUtils.getMostPopulousSwatch(palette)?.let {
            statusBarColor = ColorUtils.scrimify(it.rgb, isDark, SCRIM_ADJUSTMENT)
            // set a light status bar
            if (!isDark) {
                ViewUtils.setLightStatusBar(shot)
            }
        }

        if (statusBarColor != window.statusBarColor) {
//            shot.setScrimColor(statusBarColor)
            ValueAnimator.ofArgb(window.statusBarColor, statusBarColor).apply {
                addUpdateListener { animation ->
                    window.statusBarColor = animation.animatedValue as Int
                }
                duration = 1000L
                interpolator = getFastOutSlowInInterpolator(this@TransitionDestActivity)
            }.start()
        }
    }


    override fun onResume() {
        super.onResume()
        draggableFrame.addListener(chromeFader);

    }

    override fun onPause() {
        super.onPause()
        draggableFrame.removeListener(chromeFader);
    }

    fun calImgHeightByWith(with: Int, height: Int, expectWith: Int): Int {
        if (height == 0) {
            return expectWith
        }
        var h = (height * (expectWith.toFloat() / with)).toInt()
        if (expectWith > h) {
            h = expectWith
        }
        if (expectWith.toFloat() / h < 3f / 4f) {
            h = expectWith * 4 / 3
        }
        return h
    }


    companion object {

        private const val SCRIM_ADJUSTMENT = 0.075f

        fun start(
                context: Activity,
                view: View,
                image: String
        ) {

            val options = ActivityOptions.makeSceneTransitionAnimation(
                    context,
                    Pair.create(view, context.getString(R.string.transition_shot)),
                    Pair.create(view, context.getString(R.string.transition_shot_background))
            )
            val intent = Intent(context, TransitionDestActivity::class.java)
            intent.putExtra("image", image)
            context.startActivity(intent, options.toBundle())

        }
    }
}





