package com.hzy.water

import android.animation.*
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import java.util.*


/**
 * 支付宝蚂蚁森林水滴能量
 * Created by ziye_huang on 2018/12/29.
 */
open class WaterContainer : FrameLayout {

    private var mOnWaterItemListener: OnWaterItemListener? = null
    /**
     * 小树坐标X
     */
    private var mTreeCenterX = 0f
    /**
     * 小树坐标Y
     */
    private var mTreeCenterY = 0f
    /**
     * 是否正在收集能量
     */
    private var mIsCollecting = false

    private val WHAT_ADD_PROGRESS = 1
    /**
     * view 变化的 y 抖动范围
     */
    private val CHANGE_RANGE = 10
    /**
     * 控制抖动动画执行的快慢
     */
    val PROGRESS_DELAY_MILLIS = 12
    /**
     * 控制水滴动画的偏移量
     */
    private val mOffsets = mutableListOf(5.0f, 4.5f, 4.8f, 5.5f, 5.8f, 6.0f, 6.5f)
    private val mRandom = Random()
    private var mWidth = 0f
    private var mHeight = 0f
    private var mLayoutInflater: LayoutInflater
    private var mWaterList: List<Water>? = null


    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        mLayoutInflater = LayoutInflater.from(context)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param list 数据集合
     */
    fun setWaterList(list: List<Water>, treeCenterX: Float, treeCenterY: Float) {
        this.mWaterList = list
        this.mTreeCenterX = treeCenterX
        this.mTreeCenterY = treeCenterY
        removeAllViews()
        post { addWaterView(list) }
    }

    /**
     * 设置小球数据，根据数据集合创建小球数量
     *
     * @param list 数据集合
     * @param view
     */
    fun setWaterList(list: List<Water>, view: View) {
        this.mWaterList = list
        this.mTreeCenterX = view.x
        this.mTreeCenterY = view.y
        removeAllViews()
        post { addWaterView(list) }
    }

    fun getWaterList(): List<Water>? {
        return mWaterList
    }

    private fun addWaterView(list: List<Water>) {
        val xRandom = randomCommon(1, 8, list.size)
        val yRandom = randomCommon(1, 7, list.size)
        if (xRandom == null || yRandom == null) {
            return
        }
        for (i in list.indices) {
            val water = list[i]
            val view = mLayoutInflater.inflate(R.layout.item_water, this, false) as TextView
            view.x = (mWidth * xRandom[i] * 0.11).toFloat()
            view.y = (mHeight * yRandom[i] * 0.08).toFloat()
            view.tag = water
            view.text = water.content
            view.setOnClickListener { v ->
                val tag = v.tag
                if (tag is Water) {
                    mOnWaterItemListener?.let {
                        mOnWaterItemListener!!.onItemClick(tag)
                        if (tag.clickable) {
                            collectAnimator(view)
                        }
                    }
                }
            }
            view.setTag(R.string.isUp, mRandom.nextBoolean())
            setOffset(view)
            addView(view)
            addShowViewAnimation(view)
            startAnim(view)
        }
    }

    /**
     * 设置小球点击事件
     *
     * @param onWaterItemListener
     */
    fun setOnWaterItemListener(onWaterItemListener: OnWaterItemListener) {
        mOnWaterItemListener = onWaterItemListener
    }

    interface OnWaterItemListener {
        fun onItemClick(water: Water)
    }

    private fun collectAnimator(view: View) {
        if (mIsCollecting) return
        mIsCollecting = true

        val translatAnimatorX = ObjectAnimator.ofFloat(view, "translationX", getTreeCenterX())
        val translatAnimatorY = ObjectAnimator.ofFloat(view, "translationY", getTreeCenterY())
        val alphaAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translatAnimatorX, translatAnimatorY, alphaAnimator)
        animatorSet.duration = 1000
        animatorSet.start()
        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                removeView(view)
                mIsCollecting = false
            }
        })
    }

    /*fun startAnim(view: View) {
        val animator =
            ObjectAnimator.ofFloat(this, "translationY", -10f, 10f, -10f)
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.duration = 1500
        animator.start()
    }*/

    fun startAnim(view: View) {
        val isUp = view.getTag(R.string.isUp) as Boolean
        val offset = view.getTag(R.string.offset) as Float
        var mAnimator: ObjectAnimator
        if (isUp) {
            mAnimator = ObjectAnimator.ofFloat(
                view,
                "translationY",
                view.y - offset,
                view.y + offset,
                view.y - offset
            )
        } else {
            mAnimator = ObjectAnimator.ofFloat(
                view,
                "translationY",
                view.y + offset,
                view.y - offset,
                view.y + offset
            )
        }
        mAnimator.duration = 1500
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.repeatMode = ValueAnimator.RESTART
        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.start()
    }


    /**
     * 添加显示动画
     *
     * @param view
     */
    private fun addShowViewAnimation(view: View) {
        view.alpha = 0f
        view.scaleX = 0f
        view.scaleY = 0f
        view.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(500).start()
    }

    /**
     * 随机指定范围内N个不重复的数
     * 最简单最基本的方法
     *
     * @param min 指定范围最小值
     * @param max 指定范围最大值
     * @param nums   随机数个数
     */
    fun randomCommon(min: Int, max: Int, nums: Int): IntArray? {
        if (nums > max - min + 1 || max < min) {
            return null
        }
        val result = IntArray(nums)
        var count = 0
        while (count < nums) {
            val num = (Math.random() * (max - min) + min).toInt()
            var flag = true
            for (j in 0 until nums) {
                if (num == result[j]) {
                    flag = false
                    break
                }
            }
            if (flag) {
                result[count] = num
                count++
            }
        }
        return result
    }


    private fun getTreeCenterX(): Float {
        return mTreeCenterX
    }

    private fun getTreeCenterY(): Float {
        return mTreeCenterY
    }

    /**
     * 设置View的offset
     *
     * @param view
     */
    private fun setOffset(view: View) {
        val offset = mOffsets[mRandom.nextInt(mOffsets.size)]
        view.setTag(R.string.offset, offset)
    }
}