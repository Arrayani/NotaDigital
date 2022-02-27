package academy.learnprogramming.activities

import academy.learnprogramming.R
import academy.learnprogramming.databinding.ActivityRegisterBinding
import academy.learnprogramming.firestore.FirestoreClass
import academy.learnprogramming.models.UserCons
import academy.learnprogramming.utils.Constants
import academy.learnprogramming.utils.LoadingDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loglink = binding.tvLogin


        setupActionBar()
        loglink.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        //setContentView(R.layout.activity_register)

        firstnameFocusListener()
        lastnameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        conPasswordFocusListener()

        binding.btnRegister.setOnClickListener {submitForm()}


    }
    private fun submitForm() {
        binding.tilFirstName.helperText=validFirstName()
        binding.tilLastName.helperText=validlastName()
        binding.tilEmail.helperText=validEmail()
        binding.tilPassword.helperText=validPassword()
        binding.tilConfirmPassword.helperText=validconfirmPassword()
        val cekbox = binding.cbTermsAndCondition

        val validFirstname = binding.tilFirstName.helperText == null
        val validLastname = binding.tilLastName.helperText == null
        val validEmail =binding.tilEmail.helperText == null
        val validPassword = binding.tilPassword.helperText == null
        val validConPassword = binding.tilConfirmPassword.helperText == null


        /*     if (!cekbox.isChecked)
                {
                 Toast.makeText(this, "Chekbox belum di silang", Toast.LENGTH_SHORT).show()
                 return
                }*/

        if  (validFirstname && validLastname && validEmail && validPassword && validConPassword && cekbox.isChecked )
           {
               registerUser()
               resetForm()}

        else
        {
            //     Toast.makeText(this, "Chekbox belum di silang", Toast.LENGTH_SHORT).show()
            invalidForm()
        }
    }

    private fun registerUser() {
        /** initiasi firestore*/
        val mFirestore = FirebaseFirestore.getInstance()
        /** memanggil Progressbar */
        val loading = LoadingDialog(this)
            loading.startLoading()
        /**ini variable input ke database*/
        val email:String = binding.etEmail.text.toString()
        val password:String = binding.etPassword.text.toString().trim{it<=' '}
        val firstname:String = binding.etFirstName.text.toString().trim()
        val lastname : String = binding.etLastName.text.toString().trim()

        //mulai masuk firebase
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val firebaseUser: FirebaseUser = it.result!!.user!!

                    val user = UserCons(
                        firebaseUser.uid,
                        firstname,
                        lastname,
                        email
                    )
                    mFirestore.collection(Constants.USERS).document(user.id)
                        .set(user, SetOptions.merge())
                        .addOnSuccessListener {
                            /** mengakhiri progressbarrnya */
                            loading.isDismiss()
                            /** mengakses snackbar, bisa buat class tersendiri kalo mau */
                            val consLayout = binding.consLayout
                            Snackbar.make(consLayout,"berhasil register mas bro ${firebaseUser.uid}",
                                Snackbar.LENGTH_LONG).setBackgroundTint(Color.GREEN).show()
                            //sudah teregister waktunya untuk logout dan finish activity
                            FirebaseAuth.getInstance().signOut()

                            GlobalScope.launch {
                                slowMo()
                                finish()
                            }
                        }

                    }else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registering failed, please try again! ",
                        Toast.LENGTH_LONG
                    ).show()
                }
                 }}




    private fun invalidForm() {
        val cekbox = binding.cbTermsAndCondition
        var message = ""
        if(binding.tilFirstName.helperText != null)
            message += "\n\nFirstname: " + binding.tilFirstName.helperText
        //if(binding.tilLastName.helperText != null)
        //message += "\n\nLast Name: " + binding.tilLastName.helperText
        if(binding.tilEmail.helperText != null)
            message += "\n\nEmail: " + binding.tilEmail.helperText
        if(binding.tilPassword.helperText != null)
            message += "\n\nPassword: " + binding.tilPassword.helperText
        if(binding.tilConfirmPassword.helperText != null)
            message += "\n\nConfirm Password: " + binding.tilConfirmPassword.helperText
        if (binding.etConfirmPassword.text.toString() != binding.etPassword.text.toString())
            message += "\n\nPassword & Confirm Password Tidak sama "
        if (!cekbox.isChecked)
            message += "\n\nCheckbox belum di silang "

        //AlertDialog.Builder(this,R.style.AlertDialog)
        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                // do nothing
            }
            .show()
    }

    private fun resetForm() {
        binding.etFirstName.text = null
        binding.etLastName.text = null
        binding.etEmail.text = null
        binding.etPassword.text = null
        binding.etConfirmPassword.text = null
        binding.cbTermsAndCondition.isChecked=false

        binding.tilFirstName.helperText=getString(R.string.required)
        binding.tilLastName.helperText=getString(R.string.required)
        binding.tilEmail.helperText=getString(R.string.required)
        binding.tilPassword.helperText=getString(R.string.required)
        binding.tilConfirmPassword.helperText=getString(R.string.required)


 /** ini jangan dihapus, ini berguna untuk melihat value yg berhasil di input*/
 /*       var message = "Firstname: " + binding.etFirstName.text
        message += "\nLastname: " + binding.etLastName.text
        message += "\nEmail: " + binding.etEmail.text
        message += "\nPassword: " + binding.etPassword.text
        message += "\nConfirm Password: " + binding.etConfirmPassword.text
        AlertDialog.Builder(this)
            .setTitle("Form Submitted")
            .setMessage(message)
            .setPositiveButton("Okay"){_,_ ->

                binding.etFirstName.text = null
                binding.etLastName.text = null
                binding.etEmail.text = null
                binding.etPassword.text = null
                binding.etConfirmPassword.text = null
                binding.cbTermsAndCondition.isChecked=false

                binding.tilFirstName.helperText=getString(R.string.required)
                binding.tilLastName.helperText=getString(R.string.required)
                binding.tilEmail.helperText=getString(R.string.required)
                binding.tilPassword.helperText=getString(R.string.required)
                binding.tilConfirmPassword.helperText=getString(R.string.required)
            }
            .show()*/
    }

    private fun firstnameFocusListener() {
        binding.etFirstName.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.tilFirstName.helperText = validFirstName()
            }
        }
    }

    private fun validFirstName(): String? {
        val firstNameText = binding.etFirstName.text.toString()

        if (firstNameText.isEmpty())
        {
            return "Invalid Firstname"
        }
        return null
    }

    private fun lastnameFocusListener() {
        binding.etLastName.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.tilLastName.helperText = validlastName()
            }
        }
    }

    private fun validlastName(): String? {
        val lastNameText = binding.etLastName.text.toString()

        if (lastNameText.isEmpty())
        {
            return "Invalid lastname"
        }
        return null
    }


    private fun emailFocusListener() {
        binding.etEmail.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.tilEmail.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.etEmail.text.toString()
        if(!Patterns.EMAIL_ADDRESS.matcher(emailText).matches())
        {
            return "Invalid Email Address"
        }
        return null
    }

    private fun passwordFocusListener() {
        binding.etPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.tilPassword.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.etPassword.text.toString()
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        /*    if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
             {
                 return "Must Contain 1 Special Character (@#\$%^&+=)"
             }*/

        return null

    }

    private fun conPasswordFocusListener() {
        binding.etConfirmPassword.setOnFocusChangeListener { _, focused ->
            if(!focused)
            {
                binding.tilConfirmPassword.helperText = validconfirmPassword()
            }
        }

    }

    private fun validconfirmPassword(): String? {
        val passwordText = binding.etConfirmPassword.text.toString()
        if(passwordText.length < 8)
        {
            return "Minimum 8 Character Password"
        }
        if(!passwordText.matches(".*[A-Z].*".toRegex()))
        {
            return "Must Contain 1 Upper-case Character"
        }
        if(!passwordText.matches(".*[a-z].*".toRegex()))
        {
            return "Must Contain 1 Lower-case Character"
        }
        if(passwordText != binding.etPassword.text.toString())
        {
            return "Password dan Confirm Password Tidak sama"
        }
        /*    if(!passwordText.matches(".*[@#\$%^&+=].*".toRegex()))
            {
                return "Must Contain 1 Special Character (@#\$%^&+=)"
            }*/

        return null
    }

    //koding buat condition term check
    //  private fun validTerms(): String {
    /*    val terms = binding.cbTermsAndCondition
        if(terms.isChecked)
        {
            return "ini salahh?"
        }
        return null
    }*/

    private suspend fun slowMo(){
        delay(6000)
    }

    private fun setupActionBar(){
        val toolbar=binding.toolbarRegisterActivity
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar != null ){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
            actionbar.title="Back"
        }
        toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }
}
