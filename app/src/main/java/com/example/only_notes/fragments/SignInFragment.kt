package com.example.only_notes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.only_notes.R
import com.example.only_notes.databinding.FragmentSignInBinding
import com.example.only_notes.databinding.FragmentSignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController     // isse apan ek fragment to dusre fragmnt jaa sakte hai
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)  //yeh function parameter as a view lega aur isme initialize karenge hum auth wala and navController
        registerEvents()  //isme hum btn pe on click listener lagaenge aur email and password ko collect kar lenge
    }

    private fun init(view: View) {
        navControl = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {

        binding.signInToSignUp.setOnClickListener {
            navControl.navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.signInemail.text.toString()
            val pass = binding.signInpass.text.toString()

            if (email.isNullOrEmpty() || pass.isNullOrEmpty()) {
                Toast.makeText(context, "Please fill in all the Details", Toast.LENGTH_SHORT).show()
            } else{

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Logged In Successfully!!", Toast.LENGTH_SHORT)
                                .show()
                            navControl.navigate(R.id.action_signInFragment_to_homeFragment)
                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
        }
            }
        }
    }

