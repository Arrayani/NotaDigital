package academy.learnprogramming.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

//hati2 ketik AttributeSet, yg harus muncul di impor adalah android.util.AttributeSet
class MSPRadioButton(context:Context,attributeSet: AttributeSet):AppCompatRadioButton(context,attributeSet) {
init{
    applyFont()

}

    private fun applyFont() {
        val typeface : Typeface=
            Typeface.createFromAsset(context.assets,"montserratbold.ttf")
        setTypeface(typeface)
    }
}