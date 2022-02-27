package academy.learnprogramming.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextView(context : Context, attrs:AttributeSet) : AppCompatTextView(context,attrs) {
    init {
        applyfont()
    }

    private fun applyfont() {
        val typeface : Typeface =
            Typeface.createFromAsset(context.assets,"montserratregular.ttf")
        setTypeface(typeface)
    }

}