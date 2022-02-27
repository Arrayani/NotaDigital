package academy.learnprogramming.activities

import academy.learnprogramming.R
import academy.learnprogramming.databinding.ActivityLoginBinding
import academy.learnprogramming.firestore.FirestoreClass
import academy.learnprogramming.models.UserCons
import academy.learnprogramming.utils.Constants
import academy.learnprogramming.utils.LoadingDialog
import android.content.Intent
import android.net.wifi.hotspot2.pps.Credential
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

//class LoginActivity : AppCompatActivity(),View.OnClickListener {
    class LoginActivity : AppCompatActivity(){

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reglink = binding.tvRegister
        val btnLogin = binding.btnLogin
        val forgotlink = binding.tvForgotPassword

        //val button1 = findViewById<Button>(R.id.tv_register)
        //setContentView(R.layout.activity_login)*/

        emailFocusListener()
        passwordFocusListener()

        reglink.setOnClickListener{
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotlink.setOnClickListener{
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener{submitForm()}


}

private fun submitForm() {
    binding.tilEmail.helperText=validEmail()
    binding.tilPassword.helperText=validPassword()
    val validEmail =binding.tilEmail.helperText == null
    val validPassword = binding.tilPassword.helperText == null
    if (validEmail && validPassword)
    {
        logUser()
    }
    else
    {
         invalidForm()
    }
}


    private fun logUser() {
        /** memanggil Progressbar */
        val loading = LoadingDialog(this)
        loading.startLoading()
        val email:String = binding.etEmail.text.toString().trim {it<=' '}
        val password:String = binding.etPassword.text.toString().trim{it<=' '}
        //mulai masuk firebase
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                /** mengakhiri progressbarrnya */
                loading.isDismiss()
                if(it.isSuccessful){
                    FirestoreClass().getUserDetails(this@LoginActivity)

                    /** mengakses snackbar, bisa buat class tersendiri kalo mau */
                    //val consLayout = binding.consLayout
                    //Snackbar.make(consLayout,"berhasil login mas bro ",
                     //Snackbar.LENGTH_LONG).show()

                }else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Failed please try again! ",
                        Toast.LENGTH_LONG
                        ).show()
                      }
            }
    }


    private fun invalidForm() {
        var message = ""
        if(binding.tilEmail.helperText != null)
            message += "\n\nEmail: " + binding.tilEmail.helperText
        if(binding.tilPassword.helperText != null)
            message += "\n\nPassword: " + binding.tilPassword.helperText

        //AlertDialog.Builder(this,R.style.AlertDialog)
        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                // do nothing
            }
            .show()
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


    fun userLoggedInSuccess(user:UserCons){
        Log.i("First Name:",user.firstname)
        Log.i("Last Name:",user.lastname)
        Log.i("Email:",user.email)
        if(user.profileCompleted == 0){
            //if user profile is incomplete then launch the userprofileActivity
            val intent = Intent(this@LoginActivity,UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else{
            //redirect the user to main screen after login
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }



//        startActivity(Intent(this@LoginActivity,MainActivity::class.java))

    }
    /**    override fun onClick(view: View?){
        if (view != null){
            when (view.id){
                R.id.tv_forgot_password ->{

                }
                R.id.btn_login->{

                }
                R.id.btn_register->{

                    println("test button")
                    //val intent = Intent(this,RegisterActivity::class.java)
                   // startActivity(intent)
                }
            }
        }
    }*/


}