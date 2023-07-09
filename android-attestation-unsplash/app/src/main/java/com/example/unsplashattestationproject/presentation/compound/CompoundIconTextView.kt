package com.example.unsplashattestationproject.presentation.compound

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.CompoundIconTextViewBinding

class CompoundIconTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding = CompoundIconTextViewBinding.inflate(LayoutInflater.from(context))

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CompoundIconTextView).apply {
            text = getString(R.styleable.CompoundIconTextView_text) ?: ""
            icon = getDrawable(R.styleable.CompoundIconTextView_icon)
            recycle()
        }
        binding.root.setOnClickListener( ::onClick )

        addView(binding.root)
    }

    var text: CharSequence
        get() = binding.iconTextViewText.text
        set(value) {
            binding.iconTextViewText.text = value
        }

    var icon: Drawable?
        get() = binding.iconTextViewIcon.drawable
        set(value) {
            binding.iconTextViewIcon.setImageDrawable(value)
        }



    var clickListener: OnClickListener? = null

    private fun onClick(view : View) {
        clickListener?.onClick(view)
    }

}
