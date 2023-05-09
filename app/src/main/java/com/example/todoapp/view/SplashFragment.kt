package com.example.todoapp.view

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.airbnb.lottie.LottieAnimationView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSignUpBinding
import com.example.todoapp.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSplashBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sideAnimation = AnimationUtils.loadAnimation(context,R.anim.slide)
        binding.todoLogo.startAnimation(sideAnimation)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            auth = FirebaseAuth.getInstance()
            navController = Navigation.findNavController(view)
            if (auth.currentUser!= null){
                navController.navigate(R.id.action_splashFragment_to_homeFragment)
            }else{
                navController.navigate(R.id.action_splashFragment_to_signInFragment)
            }
        },2000)







    }


}