package academy.learnprogramming.activities

import academy.learnprogramming.R
import academy.learnprogramming.databinding.ActivityForgotPasswordBinding
import academy.learnprogramming.utils.LoadingDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding:ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_forgot_password)
        setupActionBar()
        emailFocusListener()
        val btnsubmit = binding.btnSubmit

        btnsubmit.setOnClickListener{submitForm()}


    }

    private fun submitForm() {
        binding.tilEmail.helperText=validEmail()
        val validEmail =binding.tilEmail.helperText == null
        if (validEmail)
        {
            sendResetlink()
        }
        else
        {
            invalidForm()
        }
    }


    private fun sendResetlink() {
        val consLayout = binding.consLayout
        /** memanggil Progressbar */
        val loading = LoadingDialog(this)
        loading.startLoading()
        val email:String = binding.etEmail.text.toString().trim {it<=' '}
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener {

                /** mengakhiri progressbarrnya */
                loading.isDismiss()
                if(it.isSuccessful){
                AlertDialog.Builder(this)
                    .setTitle("Invalid Form")
                    .setMessage("Kami telah kirim link reset password")
                    .setPositiveButton("Okay"){ _, _ ->
                        Snackbar.make(consLayout,"silahkan cek email anda ",
                            Snackbar.LENGTH_LONG).show()
                    }
                    .show()
                GlobalScope.launch {
                    slowMo()
                    finish()
                }

                }else{

                    Snackbar.make(consLayout,"Reset Password gagal, mohon di ulang kembali ",
                        Snackbar.LENGTH_LONG).show()
                }
            }
    }

    suspend fun slowMo(){
        delay(6000)
    }
    private fun invalidForm() {
        var message = ""
        if(binding.tilEmail.helperText != null)
            message += "\n\nEmail: " + binding.tilEmail.helperText


        //AlertDialog.Builder(this,R.style.AlertDialog)
        AlertDialog.Builder(this)
            .setTitle("Invalid Form")
            .setMessage(message)
            .setPositiveButton("Okay"){ _,_ ->
                // do nothing
            }
            .show()
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
    private fun setupActionBar(){

        val toolbar= binding.toolbarForgotPasswordActivity
        setSupportActionBar(toolbar)
        val actionbar= supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
            actionbar.title="Back"

        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

}