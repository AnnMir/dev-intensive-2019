package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import ru.skillbranch.devintensive.R

fun Activity.hideKeyboard() {
    val imm =
        this.applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

private var isKeyboardOpened = false
private var isKeyboardClosed = true

fun Activity.isKeyboardOpen(): Boolean {
    val rootView: View = this.findViewById(R.id.root_layout)

    val r = Rect()
    //r will be populated with the coordinates of your view that area still visible.
    rootView.getWindowVisibleDisplayFrame(r)

    val heightDiff: Int = rootView.rootView.height - (r.bottom - r.top)
    Log.i("DEVINTENSIVE", "$heightDiff")
    if (heightDiff > 100 && !isKeyboardOpened) { // if more than 100 pixels, its probably a keyboard...
        isKeyboardOpened = true
    } else if (heightDiff < 100 && isKeyboardOpened)
        isKeyboardOpened = false

    return isKeyboardOpened
}

fun Activity.isKeyboardClosed(): Boolean {
    val rootView: View = this.findViewById(R.id.root_layout)

    val r = Rect()
    //r will be populated with the coordinates of your view that area still visible.
    rootView.getWindowVisibleDisplayFrame(r)

    val heightDiff: Int = rootView.rootView.height - (r.bottom - r.top)
    Log.i("DEVINTENSIVE", "$heightDiff")
    if (heightDiff > 100 && isKeyboardClosed) { // if more than 100 pixels, its probably a keyboard...
        isKeyboardClosed = false
    } else if (heightDiff < 100 && !isKeyboardClosed)
        isKeyboardClosed = true

    return isKeyboardClosed
}