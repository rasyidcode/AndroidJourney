package me.jamilalrasyidis.prochatapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quickblox.auth.QBAuth
import com.quickblox.auth.session.QBSession
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        registerSession()

        btn_cancel.setOnClickListener {
            finish()
        }

        btn_sign_up.setOnClickListener {
            val username = et_sign_up_username.text.toString()
            val password = et_sign_up_password.text.toString()
            val fullName = et_sign_up_full_name.text.toString()

            val qbUser = QBUser(username, password)

            qbUser.fullName = fullName

            QBUsers.signUp(qbUser).performAsync(object : QBEntityCallback<QBUser> {
                override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                    Toast.makeText(baseContext, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onError(p0: QBResponseException?) {
                    Toast.makeText(baseContext, p0?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun registerSession() {
        QBAuth.createSession().performAsync(object: QBEntityCallback<QBSession> {
            override fun onSuccess(p0: QBSession?, p1: Bundle?) {

            }

            override fun onError(p0: QBResponseException?) {
                Log.e("ERROR", p0?.message.toString())
            }

        })
    }
}