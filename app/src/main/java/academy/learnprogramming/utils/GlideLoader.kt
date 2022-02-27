package academy.learnprogramming.utils

import academy.learnprogramming.R
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context:Context) {
    fun loadUserPicture(imageURI: Uri, imageView:ImageView){
        try{
            //load the user image in the ImageView
            Glide
                .with(context)
                //.load(Uri.parse(imageURI.toString()))//URI of the image
                .load(imageURI)//URI of the image
                .centerCrop()//scale type of the image
                .placeholder(R.drawable.ic_user_placeholder)// The default place holder if image is failed to load
                .into(imageView)//the view in wich the image will be loaded
        }catch (e:IOException){
            e.printStackTrace()
        }
    }
}