package com.example.drawingappkothlin

import android.content.Context
import android.graphics.*
import android.graphics.Paint.DITHER_FLAG
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet?) : View(context,attrs) {

    private var mDrawPath : CustomPath? =null
    private var mCanvasBitmap : Bitmap?=null
    private var mDrawPaint : Paint?=null
    private var mCanvasPaint : Paint?=null
    private var mBrushSize:Float=0.toFloat()
    private var color= Color.BLACK;
    private var canvas:Canvas?=null

    init {
        setupDrawing()
    }

   private fun setupDrawing(){

       mDrawPaint= Paint();
       mDrawPath=CustomPath(color,mBrushSize)
       mDrawPaint!!.color=color
       mDrawPaint!!.style=Paint.Style.STROKE
       mDrawPaint!!.strokeJoin=Paint.Join.ROUND
       mDrawPaint!!.strokeCap=Paint.Cap.ROUND
       mCanvasPaint=Paint(Paint.DITHER_FLAG)
       mBrushSize=20.toFloat()

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap= Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas=Canvas(mCanvasBitmap!!)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x // Touch event of X coordinate
        val touchY = event.y // touch event of Y coordinate

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset() // Clear any lines and curves from the path, making it empty.
                mDrawPath!!.moveTo(
                    touchX,
                    touchY
                ) // Set the beginning of the next contour to the point (x,y).
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(
                    touchX,
                    touchY
                ) // Add a line from the last point to the specified point (x,y).
            }

            MotionEvent.ACTION_UP -> {
                mDrawPath = CustomPath(color, mBrushSize)
            }
            else -> return false
        }

        invalidate()
        return true
    }

   internal inner class CustomPath(var color:Int,var brushThickness:Float) : Path() {


    }
}