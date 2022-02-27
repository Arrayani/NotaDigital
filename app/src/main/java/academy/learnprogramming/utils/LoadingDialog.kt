package academy.learnprogramming.utils

import academy.learnprogramming.R
import android.app.Activity
import android.app.AlertDialog

class LoadingDialog(val mActivity:Activity) {
    private lateinit var isdialog :AlertDialog
    fun startLoading(){
     /**set view*/
        val infalter= mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.dialog_progress,null)
    /** set Dialog */
        val bulider=AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(false)
        isdialog = bulider.create()
        isdialog.show()
    }

    fun isDismiss(){
        isdialog.dismiss()
    }

}