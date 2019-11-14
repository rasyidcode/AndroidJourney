package me.jamilalrasyidis.eatit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.progress_circular
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = FirebaseFirestore.getInstance()
        val tableUser = db.collection("users")
        progress_circular.visibility = View.GONE

        btn_register.setOnClickListener {
            progress_circular.visibility = View.VISIBLE
            val phoneNumber = rgt_text_editor_phone_number.text.toString()
            val name = rgt_text_editor_name.text.toString()
            val password = rgt_text_editor_password.text.toString()

            val userDB = tableUser.document(phoneNumber)
            userDB.get().addOnSuccessListener { userdata ->
                val user = userdata.toObject(User::class.java)

                if (user != null) {
                    Toast.makeText(this, "Phone number already registered!", Toast.LENGTH_SHORT).show()
                    progress_circular.visibility = View.GONE
                } else {
                    val newUser = User(name, password)
                    tableUser.document(phoneNumber)
                        .set(newUser)
                        .addOnSuccessListener {
                            Log.d("firebase_thing", "data is successful added!")
                        }
                        .addOnFailureListener {
                            Log.d("firebase_thing", "data is failed to add")
                        }
                    Toast.makeText(this, "Data was added!", Toast.LENGTH_SHORT).show()
                    progress_circular.visibility = View.GONE

                    finish()
                }
            }
        }
    }
}
