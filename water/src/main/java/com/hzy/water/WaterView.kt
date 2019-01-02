package com.hzy.water

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.NonNull


/**
 * 自定义仿支付宝蚂蚁森林水滴View
 * Created by ziye_huang on 2018/12/29.
 */
open class WaterView : View {
    private var mPaint: Paint = Paint()
    private var mAnimator: ObjectAnimator? = null
    /**
     * 文字颜色
     */
    private val mTextColor = Color.parseColor("#69c78e")
    /**
     * 水滴填充颜色
     */
    private val mWaterFillColor = Color.parseColor("#c3f593")
    /**
     * 球描边颜色
     */
    private val mStrokeColor = Color.parseColor("#69c78e")
    /**
     * 描边线条宽度
     */
    private val mStrokeWidth = 0.5f
    /**
     * 文字字体大小
     */
    private val mTextSize = 36f
    /**
     * 水滴球半径
     */
    private val mRadius = 30
    /**
     * 圆球文字内容
     */
    private var mTextContent = ""

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, textContent: String) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.mTextContent = textContent
        mPaint.isAntiAlias = true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        drawCircleView(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // set water view width and height
        setMeasuredDimension(
            dp2px((2 * (mRadius + mStrokeWidth)).toInt()),
            dp2px((2 * (mRadius + mStrokeWidth)).toInt())
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // startAnim animation
        startAnim()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //stopAnim animation
        stopAnim()
    }

    override fun onVisibilityChanged(@NonNull changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when (visibility) {
            View.VISIBLE -> startAnim()
            else -> stopAnim()
        }
    }

    private fun drawCircleView(canvas: Canvas) {
        //圆球
        mPaint.color = mWaterFillColor
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(
            dp2px(mRadius).toFloat(),
            dp2px(mRadius).toFloat(),
            dp2px(mRadius).toFloat(),
            mPaint
        )

        //描边
        mPaint.color = mStrokeColor
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = dp2px(mStrokeWidth.toInt()).toFloat()
        canvas.drawCircle(
            dp2px(mRadius).toFloat(),
            dp2px(mRadius).toFloat(),
            dp2px((mRadius + mStrokeWidth).toInt()).toFloat(),
            mPaint
        )

        //圆球文字
        mPaint.textSize = mTextSize
        mPaint.color = mTextColor
        mPaint.style = Paint.Style.FILL
        drawVerticalText(canvas, dp2px(mRadius).toFloat(), dp2px(mRadius).toFloat(), mTextContent)
    }

    private fun drawVerticalText(canvas: Canvas, centerX: Float, centerY: Float, text: String) {
        val fontMetrics = mPaint.fontMetrics
        val baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2
        val textWidth = mPaint.measureText(text)
        val startX = centerX - textWidth / 2
        val endY = centerY + baseLine
        canvas.drawText(text, startX, endY, mPaint)
    }


    fun startAnim() {
        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, "translationY", -6.0f, 6.0f, -6.0f)
            mAnimator!!.duration = 3500
            mAnimator!!.interpolator = LinearInterpolator()
            mAnimator!!.repeatMode = ValueAnimator.RESTART
            mAnimator!!.repeatCount = ValueAnimator.INFINITE
            mAnimator!!.start()
        } else if (!mAnimator!!.isStarted) {
            mAnimator!!.start()
        }
    }

    fun stopAnim() {
        mAnimator?.let {
            mAnimator!!.cancel()
            mAnimator = null
        }
    }

    private fun dp2px(dp: Int): Int {
        return (resources.displayMetrics.density * dp + 0.5f).toInt()
    }

    private fun px2dp(px: Int): Int {
        return (px / resources.displayMetrics.density + 0.5f).toInt()
    }
}