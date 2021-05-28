package me.jamilalrasyidis.firebase_messages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class SignInActivity : AppCompatActivity() {

    private val constraintLayout by lazy { findViewById<ConstraintLayout>(R.id.constraint_layout) }
    private val signInBtn by lazy { findViewById<Button>(R.id.btn_account_sign) }

    private val RC_SIGN_IN = 1

    private val signInProviders = listOf(
        AuthUI.IdpConfig.EmailBuilder()
            .setAllowNewAccounts(true)
            .setRequireName(true).build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signInBtn.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.drawable.fire_image)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            when(resultCode) {
                Activity.RESULT_OK -> {
                    val progressDialog = indeterminateProgressDialog("Setting up your account")
                    // TODO: Initialize current user in the Firestore
                    startActivity(intentFor<MainActivity>().newTask().clearTask())
                    progressDialog.dismiss()
                }
                Activity.RESULT_CANCELED -> {
                    if (response == null) return

                    when(response.error?.errorCode) {
                        ErrorCodes.NO_NETWORK -> longSnackbar(constraintLayout, "No network")
                        ErrorCodes.UNKNOWN_ERROR -> longSnackbar(constraintLayout, "Unknown Error")
                    }
                }
            }
        }
    }

}