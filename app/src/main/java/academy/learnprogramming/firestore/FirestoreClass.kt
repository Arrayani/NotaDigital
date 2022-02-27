package academy.learnprogramming.firestore
import academy.learnprogramming.activities.LoginActivity
import academy.learnprogramming.activities.RegisterActivity
import academy.learnprogramming.activities.UserProfileActivity
import academy.learnprogramming.models.UserCons
import academy.learnprogramming.utils.Constants
import academy.learnprogramming.utils.LoadingDialog
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {
private val mFirestore = FirebaseFirestore.getInstance()

    fun getCurrentUserID():String {
        val currentUser = FirebaseAuth.getInstance().currentUser

        //a variable to assign the currentuserId if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        println("look the code")
        //here we pass the collection name from which we want the data
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                //ini kita sudah menerima dokumen snapshot yg kita panggil, setelah itu harus
                //di konversi menjadi bentuk UserCons yg kita buat sebelumnya
                val user= document.toObject(UserCons::class.java)!!
                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.NOTADIGITAL_PREFERENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                //Key :Value  logged_in_username: firstname lastname

                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstname} ${user.lastname}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
                Log.e(activity.javaClass.simpleName,
                    "Error while getting user details"
                )

            }
    }

    fun updateUserProfileData(activity: Activity,userHashMap:HashMap<String,Any>){
        val loading = LoadingDialog(activity)
        loading.startLoading()
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity ->{
                        loading.isDismiss()
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{
                when(activity){
                    is UserProfileActivity ->{
                        loading.isDismiss()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details"
                )
            }
    }
    fun uploadImageToCloudStorage(activity: Activity,imageFileURI: Uri?){
        val sRef:StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE+System.currentTimeMillis()+"."
            + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener {taskSnapshot->
            //the image upload is success
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            //get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener {uri->
                    Log.e("Downloadable image URL",uri.toString())
                    when(activity){
                        is UserProfileActivity->{
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }

                }
        }
            .addOnFailureListener{exception->
                when(activity){
                    is UserProfileActivity ->{
                        println("gagal upload")
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
}