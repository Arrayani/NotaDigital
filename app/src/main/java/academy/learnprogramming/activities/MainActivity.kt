package academy.learnprogramming.activities

import academy.learnprogramming.R
import academy.learnprogramming.databinding.ActivityMainBinding
import academy.learnprogramming.utils.Constants
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
/**penulisan binding.root, sebaiknya setelah R.layout.activity main
 karena penggunan binding.textview nya ga sesuai perintah */

//penulisan sharedPreferences ini perulangan kalimat yg ada di firestore class
// ga usah ribet, just follow along
        val sharedPreferences=getSharedPreferences(Constants.NOTADIGITAL_PREFERENCES,Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!
        binding.tvMain.text="Halo $username"


 //        val mtest = findViewById<TextView>(R.id.tv_main)
//        mtest.text="ini dari findview"



    }
}