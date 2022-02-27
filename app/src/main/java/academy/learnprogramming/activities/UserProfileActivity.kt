package academy.learnprogramming.activities

import academy.learnprogramming.R
import academy.learnprogramming.databinding.ActivityUserProfileBinding
import academy.learnprogramming.firestore.FirestoreClass
import academy.learnprogramming.models.UserCons
import academy.learnprogramming.utils.Constants
import academy.learnprogramming.utils.Constants.READ_STORAGE_PERMISSION_CODE
import academy.learnprogramming.utils.GlideLoader
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.io.IOException


class UserProfileActivity : AppCompatActivity(), View.OnClickListener {
    private var userDetails = UserCons()
    private lateinit var binding:ActivityUserProfileBinding
    private var mSelectedImageFileUri: Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_user_profile)

     //   var userDetails = UserCons()
//        bisa di ketik seperti ini if (intent.hasExtra("myName")){
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
//            get the user details  from intent  as a parcelableextra... lihatt ada !!!!
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
         binding.etFirstName.isEnabled=false
         binding.etFirstName.setText(userDetails.firstname)

         binding.etLastName.isEnabled=false
         binding.etLastName.setText(userDetails.lastname)

         binding.etEmail.isEnabled=false
         binding.etEmail.setText(userDetails.email)


        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)
        binding.btnSubmit.setOnClickListener(this@UserProfileActivity)

//        ContextCompat.checkSelfPermission(this@UserProfileActivity, READ_EXTERNAL_STORAGE)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when (v.id){
                R.id.iv_user_photo ->{

                   if(ContextCompat.checkSelfPermission(
                           this@UserProfileActivity, READ_EXTERNAL_STORAGE
                       ) == PackageManager.PERMISSION_GRANTED
                   ) {
                      Constants.showImageChooser(this@UserProfileActivity)
                       /*val consLayout = binding.consLayout
                       Snackbar.make(consLayout,"You are already have the storage permission",
                           Snackbar.LENGTH_LONG).setBackgroundTint(Color.GREEN).show()*/
                       }else{
                       requestStoragePermission()
                             }
                }
                R.id.btn_submit->{

                    FirestoreClass().uploadImageToCloudStorage(this@UserProfileActivity,mSelectedImageFileUri)

                   /* if(validateUserProfileDetails()){

                        val userHashMap = HashMap<String,Any>()
                        val mobileNumber= binding.etMobileNumber.text.toString().trim(){it <=' '}
                        val gender = if (binding.rbMale.isChecked){
                            Constants.MALE
                        }else{
                            Constants.FEMALE
                        }
                        if(mobileNumber.isNotEmpty()){
                            userHashMap[Constants.MOBILE]=mobileNumber.toLong()
                        }
                        //key:gender value : male/female
                        userHashMap[Constants.GENDER] = gender

                        FirestoreClass().updateUserProfileData(this,userHashMap)
                       *//* val consLayout = binding.consLayout
                        Snackbar.make(consLayout,"Your details are valid, you can update them",
                            Snackbar.LENGTH_LONG).setBackgroundTint(Color.GREEN).show()*//*
                    }*/
                }


            }
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                READ_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed to update the profile pict")
                .setPositiveButton("ok") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this@UserProfileActivity, arrayOf(
                            READ_EXTERNAL_STORAGE
                        ), READ_STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton("cancel") { dialog, _ -> dialog.dismiss() }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE),
                READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               /* Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show()*/
                Constants.showImageChooser(this@UserProfileActivity)
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode ==  Activity.RESULT_OK)
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE ){
                if (data != null){
                    try{
                        //the uri of selected image from phone storage.
                       // val selectedImageFileUri =  data.data!!
                           mSelectedImageFileUri=data.data!!
                       // binding.ivUserPhoto.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        val ivUserPhoto = binding.ivUserPhoto
                        GlideLoader(this@UserProfileActivity).loadUserPicture(mSelectedImageFileUri!!,ivUserPhoto)
                       // GlideLoader(this@UserProfileActivity).loadUserPicture(selectedImageFileUri,ivUserPhoto)

                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            }
    }

    private fun validateUserProfileDetails():Boolean{
        val etnumber = binding.etMobileNumber.text.toString().trim(){it <=' '}
        return when{
            TextUtils.isEmpty(etnumber)->{
                val consLayout = binding.consLayout
                Snackbar.make(consLayout,"Please input a valid phone number",
                    Snackbar.LENGTH_LONG).setBackgroundTint(Color.RED).show()
                false
            }
            else ->{
                true
            }
        }
    }

    fun userProfileUpdateSuccess(){
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(this@UserProfileActivity,MainActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL:String){
        //hide progressbar
            Toast.makeText(
                this@UserProfileActivity,
                "Your image is Uploaded successfully.image URL is $imageURL",
                Toast.LENGTH_LONG
            ).show()
    }
}