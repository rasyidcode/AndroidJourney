package me.jamilalrasyidis.eatit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // init firebase
        val db = FirebaseFirestore.getInstance()
        val tableUser = db.collection("users")
        progress_circular.visibility = View.GONE

        btn_login.setOnClickListener {
            progress_circular.visibility = View.VISIBLE

            if (phone_number_edit_text.toString() != "" && password_edit_text.text.toString() != "") {
                val userDB = tableUser.document(phone_number_edit_text.text.toString())
                userDB.get().addOnSuccessListener { userdata ->
                    val user = userdata.toObject(User::class.java)

                    if (user != null) {
                        if (user.password == password_edit_text.text.toString()) {
                            Toast.makeText(this, "Sign in successfully !", Toast.LENGTH_SHORT).show()

                            progress_circular.visibility = View.GONE
                            Common.currentUser = user
                            Log.d(TAG, Common.currentUser.name)
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java).apply {
                                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            })
                            finish()
                        } else {
                            Toast.makeText(this, "Sign in failed !", Toast.LENGTH_SHORT).show()

                            progress_circular.visibility = View.GONE
                        }
                    } else {
                        Toast.makeText(this, "User not found !", Toast.LENGTH_SHORT).show()

                        progress_circular.visibility = View.GONE
                    }
                }.addOnFailureListener { error ->
                    Log.d("firebase_thing", "$error")
                }
            } else {
                Toast.makeText(this, "Phone Number and Password cannot be empty !", Toast.LENGTH_SHORT).show()
                progress_circular.visibility = View.GONE
            }
        }
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}
