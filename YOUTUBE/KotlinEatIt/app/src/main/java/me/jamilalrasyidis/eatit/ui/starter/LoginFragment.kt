package me.jamilalrasyidis.eatit.ui.starter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import me.jamilalrasyidis.eatit.*
import me.jamilalrasyidis.eatit.data.User
import me.jamilalrasyidis.eatit.databinding.FragmentLoginBinding
import me.jamilalrasyidis.eatit.ui.home.HomeActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        binding.apply {

            btnLogin.visibility = View.VISIBLE

            btnLogin.setOnClickListener {

                val phoneNumber = phoneNumberEditText.text.toString()
                val password = passwordEditText.text.toString()

                progressCircular.visibility = View.VISIBLE

                if (phoneNumber != "" && password != "") {
                    val userData = usersRef.document(phoneNumber)
                    userData.get().addOnSuccessListener {
                        val user = it.toObject(User::class.java)

                        if (user != null) {
                            if (user.password == password) {
                                activity?.toast("Login successfully !")
                                Common.currentUser = user
                                startActivity(activity?.intentFor<HomeActivity>()?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                progressCircular.visibility = View.GONE

                                activity?.finish()
                            } else {
                                activity?.toast("Login failed !")
                                progressCircular.visibility = View.GONE
                            }
                        } else {
                            activity?.toast("User doesn't exist !")
                            progressCircular.visibility = View.GONE
                        }
                    }.addOnFailureListener {
                        Log.d(TAG, "${it.message}")
                    }
                } else {
                    activity?.longToast("Phone number or Password cannot be empty !")
                    progressCircular.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val TAG = "LoginFragment"
    }

}