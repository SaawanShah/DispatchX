package com.clickandsave.saawanshah.dispatchnews.Fragments.Splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.clickandsave.saawanshah.dispatchnews.R
import com.clickandsave.saawanshah.dispatchnews.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnStart.setOnClickListener {

            val currentUser = FirebaseAuth.getInstance().currentUser

            if (currentUser != null) {
                // User already logged in
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

            } else {
                // User not logged in
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            }


        }

        // Set initial states for animation
        binding.tvTitle.alpha = 0f
        binding.tvTitle.translationY = 40f

        binding.titleDivider.alpha = 0f
        binding.titleDivider.translationY = 40f

        binding.tvTag.alpha = 0f
        binding.tvTag.translationY = 40f

        binding.btnStart.alpha = 0f
        binding.btnStart.translationY = 80f

        // Run staggered animations
        binding.tvTitle.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(200)
            .start()

        binding.titleDivider.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(350)
            .start()

        binding.tvTag.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(500)
            .start()

        binding.btnStart.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(650)
            .start()
    }
}