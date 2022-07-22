package com.tm968.shoppev4.page.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class Keyboard(private var activity: Activity) {
    @SuppressLint("ClickableViewAccessibility")
    fun setUpActionKeyBoard(view: View) {
        // Set up touch listener for non-text box views to hide keyboard.
        // Set up touch listener for non-text box views to hide keyboard.
        if (view !is EditText) {
            view.setOnTouchListener { v, event ->
                hideSoftKeyboard(activity)
                false
            }
        }
        //If a layout container, iterate over children and seed recursion.
        //If a layout container, iterate over children and seed recursion.
        if (view is ViewGroup) {
            val viewGroup = (view as ViewGroup)
            for (i in 0 until viewGroup.childCount) {
                val innerView = viewGroup.getChildAt(i)
                setUpActionKeyBoard(innerView as View)
            }
        }
    }

    fun hideSoftKeyboard(activity: Activity) {
        val focusedView = activity.currentFocus
        if (focusedView != null) {
            focusedView.clearFocus()
            val inputMethodManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
        }
    }
}