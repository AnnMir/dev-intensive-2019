package ru.skillbranch.devintensive.ui.custom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.animation.doOnRepeat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.extensions.pxToDp
import kotlin.math.max

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
    private var initials: String = "??"

    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val initialsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val viewRect = Rect()
    private val borderRect = Rect()
    private var size = 0

    private var isAvatarMode = true

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
            initials = ta.getString(R.styleable.CircleImageView_cv_initials) ?: "??"
            ta.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
        setOnLongClickListener { handleLongClick() }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(max(initSize, size), max(initSize, size))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
    }

    override fun onDraw(canvas: Canvas) {
        // NOT allocate only draw
        if (drawable != null && isAvatarMode) {
            drawAvatar(canvas)
        } else {
            drawInitials(canvas)
        }
        //resize rect
        val half = (borderWidth / 2).toInt()
        borderRect.set(viewRect)
        borderRect.inset(half, half)
        canvas.drawOval(borderRect.toRectF(), borderPaint)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.isAvatarMode = isAvatarMode
        savedState.borderWidth = borderWidth
        savedState.borderColor = borderColor
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state)
            isAvatarMode = state.isAvatarMode
            borderWidth = state.borderWidth
            borderColor = state.borderColor
            with(borderPaint) {
                color = borderColor
                strokeWidth = borderWidth
            }
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (isAvatarMode) prepareShader(width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (isAvatarMode) prepareShader(width, height)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (isAvatarMode) prepareShader(width, height)
    }

    fun setInitials(initials: String) {
        this.initials = initials
        if (!isAvatarMode) {
            invalidate()
        }
    }

    fun setBorderColor(@ColorInt color: Int) {
        borderColor = color
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderColor(colorRes: String) {
        borderColor = Color.parseColor(colorRes)
        borderPaint.color = borderColor
        invalidate()
    }

    fun getBorderColor(): Int {
        return borderColor
    }

    fun setBorderWidth(@Dimension width: Int) {
        borderWidth = context.dpToPx(width)
        borderPaint.strokeWidth = borderWidth
        invalidate()
    }

    fun getBorderWidth(): Int {
        return context.pxToDp(borderWidth)
    }


    private fun resolveDefaultSize(spec: Int): Int {
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SIZE).toInt() // resolveDefaultSize
            MeasureSpec.AT_MOST -> MeasureSpec.getSize(spec) //from spec
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec) //from spec
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun setup() {
        with(borderPaint) {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = borderColor
        }
    }

    private fun prepareShader(w: Int, h: Int) {
        // prepare buffer this
        if (w == 0 || drawable == null) return
        val srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(srcBm, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
    }

    private fun drawAvatar(canvas: Canvas) {
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(canvas: Canvas) {
        //TODO set colorAccent from attrs initialsPaint.color =
        canvas.drawOval(viewRect.toRectF(), initialsPaint)
        with(initialsPaint) {
            color = Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = height * 0.33f
        }
        val offsetY = (initialsPaint.descent() + initialsPaint.ascent()) / 2
        canvas.drawText(initials, viewRect.exactCenterX(), viewRect.exactCenterY() - offsetY, initialsPaint)
    }

    private fun handleLongClick(): Boolean {
        val va = ValueAnimator.ofInt(width, width * 2).apply {
            duration = 600
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
        }

        va.addUpdateListener {
            size = it.animatedValue as Int
            requestLayout()
        }
        va.doOnRepeat { toggleMode() }
        va.start()
        return true
    }

    private fun toggleMode() {
        isAvatarMode = !isAvatarMode
        invalidate()
    }

    private class SavedState : BaseSavedState, Parcelable {
        var isAvatarMode: Boolean = true
        var borderWidth: Float = 0f
        var borderColor: Int = 0

        constructor(superState: Parcelable?) : super(superState) {

        }

        constructor(src: Parcel) : super(src) {
            //restore state from parcel

            isAvatarMode = src.readInt() == 1
            borderWidth = src.readFloat()
            borderColor = src.readInt()
        }

        override fun writeToParcel(dst: Parcel, flags: Int) {
            //write state to parcel

            super.writeToParcel(dst, flags)
            dst.writeInt(if (isAvatarMode) 1 else 0)
            dst.writeFloat(borderWidth)
            dst.writeInt(borderColor)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel) = SavedState(parcel)

            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}