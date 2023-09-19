package com.example.dnaloaderviewkt1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import kotlin.math.sin
import kotlin.math.PI

class DnaLoaderView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val dnaPaintX = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dnaPaintY = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dnaPaintZ = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dnaPaintW = Paint(Paint.ANTI_ALIAS_FLAG)

    private val dotRadius = 15f
    private val dotSpacing = 40f
    private val verticalGap = 20f // Add space between the lines
    private val waveFrequency = 1.2f // Adjust the frequency of the wave
    private val waveAmplitude = 100f // Adjust the amplitude of the wave

    private val dnaStrandX = mutableListOf<Pair<Float, Float>>()
    private val dnaStrandY = mutableListOf<Pair<Float, Float>>()
    private val dnaStrandZ = mutableListOf<Pair<Float, Float>>()
    private val dnaStrandW = mutableListOf<Pair<Float, Float>>()

    private lateinit var animation: Animation

    init {
        dnaPaintX.color = Color.parseColor("#ED67D4") // X wave color
        dnaPaintY.color = Color.parseColor("#5E91F7") // Y wave color
        dnaPaintZ.color = Color.parseColor("#ED67D4") // Z wave color
        dnaPaintW.color = Color.parseColor("#5E91F7") // W wave color

        dnaPaintX.style = Paint.Style.FILL
        dnaPaintY.style = Paint.Style.FILL
        dnaPaintZ.style = Paint.Style.FILL
        dnaPaintW.style = Paint.Style.FILL

        animation = createWaveAnimation()
    }

    private fun createWaveAnimation(): Animation {
        val animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                super.applyTransformation(interpolatedTime, t)
                initializeDnaStrands(interpolatedTime)
                invalidate()
            }
        }
        animation.duration = 4000 // Adjust the duration as needed
        animation.repeatCount = Animation.INFINITE
        animation.interpolator = LinearInterpolator()
        return animation
    }

    private fun initializeDnaStrands(interpolatedTime: Float) {
        val centerX = width / 2f
        val centerY = height / 2f
        val strandWidth = width / 2f

        dnaStrandX.clear()
        dnaStrandY.clear()
        dnaStrandZ.clear()
        dnaStrandW.clear()

        val dotCount = (strandWidth / dotSpacing).toInt()
        val currentTime = System.currentTimeMillis()
        val yOffset1 = centerY - dotRadius - verticalGap / 15
        val yOffset2 = centerY + dotRadius + verticalGap / 15

        for (i in 0 until dotCount) {
            val x = centerX + (i * dotSpacing) - (dotSpacing * dotCount / 2)
            val angle = 2 * PI * waveFrequency * i / dotCount + PI - currentTime / 500f + 2 * PI * interpolatedTime
            val yOffsetDelta = waveAmplitude * sin(angle).toFloat()

            // Calculate the ball size based on the Y offset
            val ballSize = if (yOffsetDelta >0.1) {
                dotRadius - (yOffsetDelta / waveAmplitude) * 6f // Decrease the ball size when going down
            } else {
                dotRadius + (yOffsetDelta / waveAmplitude) * 5f // Increase the ball size when going up
            }

            val y1 = yOffset1 - yOffsetDelta
            val y2 = yOffset2 - yOffsetDelta
            val y3 = yOffset1 + verticalGap + yOffsetDelta
            val y4 = yOffset2 + verticalGap + yOffsetDelta

            dnaStrandX.add(x to y1)
            dnaStrandY.add(x to y2)
            dnaStrandZ.add(x to y3)
            dnaStrandW.add(x to y4)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawDnaStrand(canvas, dnaStrandX, dnaPaintX)
        drawDnaStrand(canvas, dnaStrandY, dnaPaintY)
        drawDnaStrand(canvas, dnaStrandZ, dnaPaintZ)
        drawDnaStrand(canvas, dnaStrandW, dnaPaintW)
    }

    private fun drawDnaStrand(canvas: Canvas, strand: List<Pair<Float, Float>>, paint: Paint) {
        for (i in strand.indices) {
            val (x, y) = strand[i]
            canvas.drawCircle(x, y, dotRadius, paint)
        }
    }

    fun startAnimation() {
        startAnimation(animation)
    }

    fun setAnimationListener(listener: Animation.AnimationListener) {
        animation.setAnimationListener(listener)
    }
}
