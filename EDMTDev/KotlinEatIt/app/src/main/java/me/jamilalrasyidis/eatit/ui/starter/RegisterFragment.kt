package me.jamilalrasyidis.eatit.ui.starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import me.jamilalrasyidis.eatit.R
import me.jamilalrasyidis.eatit.data.User
import me.jamilalrasyidis.eatit.databinding.FragmentRegisterBinding
import org.jetbrains.anko.toast

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = Navigation.findNavController(view)

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users")

        binding.apply {

            progressCircular.visibility = View.GONE

            btnRegister.setOnClickListener {
                progressCircular.visibility = View.VISIBLE
                val phoneNumber = rgtTextEditorPhoneNumber.text.toString()
                val name = rgtTextEditorName.text.toString()
                val password = rgtTextEditorPassword.text.toString()

                val userData = userRef.document(phoneNumber)
                userData.get().addOnSuccessListener {
                    val user = it.toObject(User::class.java)
                    if (user != null)     {
                        activity?.toast("Phone number already registered !")
                        progressCircular.visibility = View.GONE
                    } else {
                        val newUser = User(name, password)
                        userRef.document(phoneNumber)
                            .set(newUser)
                            .addOnSuccessListener {
                                activity?.toast("Register successful !")
                            }.addOnFailureListener {
                                activity?.toast("Register failed !")
                            }
                        progressCircular.visibility = View.GONE
                        navHost.navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
        }
    }
}