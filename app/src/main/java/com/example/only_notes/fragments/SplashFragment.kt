package com.example.only_notes.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.only_notes.R
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var navControl: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.myLooper()!!).postDelayed({

            auth = FirebaseAuth.getInstance()
            navControl = Navigation.findNavController(view)

            if(auth.currentUser != null){              //hum check karenge ki user logged in hai ya nhi
                navControl.navigate(R.id.action_splashFragment_to_homeFragment)    //agar logged in hoga toh home main fragment pe bhej denge
            }
            else{                                                        //if not phir signIn pe bhej denge
                navControl.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        }, 2000)
    }

}