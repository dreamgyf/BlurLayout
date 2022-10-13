package com.dreamgyf.android.ui.widget.blur

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Choreographer
import android.view.Choreographer.FrameCallback
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.core.graphics.scale
import com.dreamgyf.android.utils.ui.image.bitmap.blur
import com.dreamgyf.android.utils.ui.image.drawable.RoundRectDrawable
import kotlin.math.max
import kotlin.math.min

class BlurLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    @FloatRange(from = 0.01, to = 1.0)
    private var mQuality = 1f

    @FloatRange(from = 0.0, to = 25.0)
    private var mBlurRadius = 0f

    private var mCornerRadius = 0f

    private val mCutCanvas = Canvas()

    private val mCutMatrix = Matrix()

    private val mFrameCallback: FrameCallback = object : FrameCallback {
        override fun doFrame(frameTimeNanos: Long) {
            val blurBitmap = getBlurBitmap()
            blurBitmap?.let {
                background = RoundRectDrawable(it, mCornerRadius)
            }
            Choreographer.getInstance().postFrameCallback(this)
        }
    }

    init {
        if (attrs != null) {
            val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BlurLayout)
            mQuality = typedArray.getFloat(R.styleable.BlurLayout_blurQuality, 1f)
            mBlurRadius = typedArray.getDimension(R.styleable.BlurLayout_blurRadius, 25f)
            mCornerRadius = typedArray.getDimension(R.styleable.BlurLayout_cornerRadius, 0f)
            typedArray.recycle()
        } else {
            mQuality = 1f
            mBlurRadius = 25f
            mCornerRadius = 0f
        }

        mQuality = min(max(mQuality, 0.01f), 1.0f)
        mBlurRadius = min(max(mBlurRadius, 1f), 25f)

        clipToOutline = true
    }

    private fun getBlurBitmap(): Bitmap? {
        val view = getActivityView() ?: return null
        val rectF = getPartRectF(view)
        alpha = 0f
        val bitmap = getPartBitmap(view, rectF, mQuality)
        alpha = 1f
        val blurBitmap = bitmap.blur(context, mBlurRadius)
        return if (mQuality != 1f) {
            blurBitmap.scale(
                rectF.width().toInt(),
                rectF.height().toInt()
            )
        } else {
            blurBitmap
        }
    }

    private fun getActivityView(): View? {
        val context = context
        var activity: Activity? = null
        if (context is Activity) {
            activity = context
        } else if (context is ContextWrapper) {
            val baseContext = context.baseContext
            if (baseContext is Activity) {
                activity = baseContext
            }
        }
        return activity?.window?.decorView?.findViewById(android.R.id.content)
    }

    private fun getPartBitmap(rootView: View, rectF: RectF, quality: Float): Bitmap {
        val bitmap = Bitmap.createBitmap(
            (quality * rectF.width()).toInt(),
            (quality * rectF.height()).toInt(),
            Bitmap.Config.ARGB_8888
        )
        mCutCanvas.setBitmap(bitmap)
        mCutMatrix.reset()
        mCutMatrix.preScale(quality, quality)
        mCutMatrix.postTranslate(-rectF.left * quality, -rectF.top * quality)
        mCutCanvas.setMatrix(mCutMatrix)
        rootView.draw(mCutCanvas)
        return bitmap
    }

    private fun getPartRectF(rootView: View): RectF {
        val location = getLocationWithRootView(rootView, this)
        return RectF(location[0], location[1], location[0] + width, location[1] + height)
    }

    private fun getLocationWithRootView(rootView: View, view: View): FloatArray {
        val location = FloatArray(2)
        val rootScreenLocation = IntArray(2)
        rootView.getLocationOnScreen(rootScreenLocation)
        val screenLocation = IntArray(2)
        view.getLocationOnScreen(screenLocation)
        location[0] = (screenLocation[0] - rootScreenLocation[0]).toFloat()
        location[1] = (screenLocation[1] - rootScreenLocation[1]).toFloat()
        return location
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Choreographer.getInstance().postFrameCallback(mFrameCallback)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(mFrameCallback)
    }
}