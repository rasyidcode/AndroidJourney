package me.jamilalrasyidis.eatit.ui.starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import me.jamilalrasyidis.eatit.R
import me.jamilalrasyidis.eatit.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHost = Navigation.findNavController(view)

        binding.apply {

            btnLogin.setOnClickListener {
                navHost.navigate(R.id.action_mainFragment_to_loginFragment)
            }

            btnRegister.setOnClickListener {
                navHost.navigate(R.id.action_mainFragment_to_registerFragment)
            }

        }
    }

}