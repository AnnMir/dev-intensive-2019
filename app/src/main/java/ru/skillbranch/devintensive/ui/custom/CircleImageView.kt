package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import android.graphics.BitmapShader
import android.graphics.Bitmap
import android.graphics.Paint
import kotlin.math.ceil


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH

    init {
        if (attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor,
                DEFAULT_BORDER_COLOR
            )
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth,
                DEFAULT_BORDER_WIDTH)
            a.recycle()
        }
    }

    @Dimension
    fun getBorderWidth(): Int {
        return borderWidth
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidth = dp
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = colorId
    }

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

}