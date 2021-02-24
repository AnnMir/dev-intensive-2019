package ru.skillbranch.devintensive.extensions

import android.content.Context

fun Context.dpToPx(dp: Int): Float = dp.toFloat() * this.resources.displayMetrics.density


fun Context.pxToDp(px: Float): Int = (px / this.resources.displayMetrics.density).toInt()