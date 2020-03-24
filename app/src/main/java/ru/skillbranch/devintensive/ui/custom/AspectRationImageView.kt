package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import ru.skillbranch.devintensive.R

class AspectRationImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
    ) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object{
        private const val DEFAULT_ASPECT_RATION = 1.78f
    }
    private var aspectRatio = DEFAULT_ASPECT_RATION

    init{
        if(attrs != null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
            aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATION)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredWidth/aspectRatio).toInt()
        setMeasuredDimension(measuredWidth, newHeight)
    }
}