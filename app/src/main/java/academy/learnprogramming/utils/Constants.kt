package academy.learnprogramming.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.webkit.MimeTypeMap.getSingleton

object Constants {
    const val USERS: String = "users"
    const val NOTADIGITAL_PREFERENCES: String = "NotaDigitalPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MALE:String = "Male"
    const val FEMALE:String = "Female"
    const val MOBILE:String = "mobile"
    const val GENDER:String = "gender"
    const val USER_PROFILE_IMAGE:String = "User_Profile_Image"

    fun showImageChooser(activity:Activity){
        //An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(activity: Activity,uri: Uri?):String?{
        /* MimetypeMap: Two-way map that maps  MIME-types to file extensions and vice versa
        getsingleton : buat singleton instance of mimetypemap
        getextensionfrommimetype : return the registerd extension  for the given mime type
        contentresolver.gettipe : return the MIME type of the given contenr url
        * */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}