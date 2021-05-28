package me.jamilalrasyidis.prochatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.quickblox.auth.session.QBSettings
import com.quickblox.core.QBEntityCallback
import com.quickblox.core.exception.QBResponseException
import com.quickblox.users.QBUsers
import com.quickblox.users.model.QBUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeFramework()

        btn_sign_up.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
        }

        btn_login.setOnClickListener {
            val username = et_login_username.text.toString()
            val password = et_login_password.text.toString()
            val qbUser = QBUser(username, password)
            QBUsers.signIn(qbUser).performAsync(object: QBEntityCallback<QBUser> {
                override fun onSuccess(p0: QBUser?, p1: Bundle?) {
                    Toast.makeText(this@MainActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                }

                override fun onError(p0: QBResponseException?) {
                    Toast.makeText(this@MainActivity, p0?.message.toString(), Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

    private fun initializeFramework() {
        QBSettings.getInstance().init(applicationContext, APP_ID, AUTH_KEY, AUTH_SECRET)
        QBSettings.getInstance().accountKey = ACCOUNT_KEY
    }

    companion object {
        const val APP_ID = "79032"
        const val AUTH_KEY = "vSj6A7aJKzXPOVQ"
        const val AUTH_SECRET = "xQDceGnub9GB5W7"
        const val ACCOUNT_KEY = "6xErsuwshMvVJU14Dy1m"
    }
}
