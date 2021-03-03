package ru.skillbranch.devintensive.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.extensions.pxToDp
import kotlin.math.max
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_SIZE = 40
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2
    }

    @SuppressLint("SupportAnnotationUsage")
    @Px
    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
    @ColorInt
    private var borderColor: Int = DEFAULT_BORDER_COLOR

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var clipPath = Path()
    private var size = 0

    init {
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = ta.getColor(
                R.styleable.CircleImageView_cv_borderColor,
                DEFAULT_BORDER_COLOR
            )
            borderWidth = ta.getDimension(
                R.styleable.CircleImageView_cv_borderWidth,
                context.dpToPx(DEFAULT_BORDER_WIDTH)
            )
            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(max(initSize, size), max(initSize, size))
    }

    override fun onDraw(canvas: Canvas?) {
        val x = width / 2f
        val y = height / 2f
        val radius = min(x, y)

        clipPath.addCircle(x, y, radius, Path.Direction.CW)
        canvas?.clipPath(clipPath)

        super.onDraw(canvas)
        drawCircle(canvas, x, y, radius)
    }

    private fun drawCircle(canvas: Canvas?, x: Float, y: Float, radius: Float) {
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderWidth

        canvas?.drawCircle(x, y, radius, paint)
    }

    @SuppressLint("ResourceType")
    fun setBorderColor(@ColorInt colorId: Int) {
        borderColor = resources.getColor(colorId,context.theme)
        invalidate()
    }

    fun setBorderColor(colorRes: String) {
        borderColor = Color.parseColor(colorRes)
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderWidth(@Dimension width: Int) {
        borderWidth = context.dpToPx(width)
        invalidate()
    }

    fun getBorderWidth(): Int = context.pxToDp(borderWidth)

    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt() // resolveDefaultSize
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec) //from spec
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec) //from spec
            else -> MeasureSpec.getSize(spec)
        }
    }
}