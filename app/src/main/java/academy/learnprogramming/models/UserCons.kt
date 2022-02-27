package academy.learnprogramming.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**kalo di perhatikan ini mirip hashmap
 bedanya ga pakai kalimat "to"  jadi si pemanggil
 mengirim value yg mereka punya, dan ketika value nya si pemanggil tidak lengkap
 value yg digunakan adalah value defaultnya yg di deklarasi contonnya long =0 */

@Parcelize
class UserCons (
 val id:String="",
 val firstname : String = "",
 val lastname : String = "",
 val email : String = "",
 val image : String = "",
 val mobile : Long = 0,
 val gender : String = "",
 val profileCompleted: Int =0
 ):Parcelable
