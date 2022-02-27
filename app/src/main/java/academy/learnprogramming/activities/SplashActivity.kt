package academy.learnprogramming.activities

import academy.learnprogramming.databinding.ActivitySplashBinding
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import academy.learnprogramming.R
//import kotlinx.android.synthetic.main.activity_splash.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val view=binding.ivNote
     //   val facetype=binding.tvAppName

        //setContentView(R.layout.activity_splash)

        view.alpha = 0f
        view.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
       // val typeface : Typeface = Typeface.createFromAsset(assets,"font/montserratbold.ttf")
      //  facetype.typeface = typeface


    }
}