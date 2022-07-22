package com.tm968.shoppev4.page.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager(context:Context?,attrs:AttributeSet?)
    : ViewPager(context!!,attrs){
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}