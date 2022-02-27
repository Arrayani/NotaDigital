package academy.learnprogramming.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MSPEditText(context : Context, attrs: AttributeSet) : AppCompatEditText(context,attrs) {
    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface : Typeface =
            Typeface.createFromAsset(context.assets,"montserratregular.ttf")
        setTypeface(typeface)
    }
}