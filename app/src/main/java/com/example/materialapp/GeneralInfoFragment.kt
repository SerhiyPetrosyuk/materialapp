package com.example.materialapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.materialapp.databinding.FragmentGeneralInfoBinding

class GeneralInfoFragment : Fragment() {

    lateinit var binding: FragmentGeneralInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGeneralInfoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageStar.animateVectorDrawableInfinite(R.drawable.avd_anim_start)
    }

}