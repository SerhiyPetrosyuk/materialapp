package com.example.materialapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.materialapp.databinding.DialogSetTimeBinding
import com.example.materialapp.databinding.FragmentStubBinding
import com.example.materialapp.databinding.WidgetChoiseChipBinding
import com.google.android.material.chip.Chip

class StubFragment : Fragment() {

    lateinit var binding: FragmentStubBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStubBinding.inflate(inflater)

        binding.buttonGoToOnboarding.setOnClickListener {
            startActivity(Intent(requireContext(), OnboardingActivity::class.java))
        }

        binding.buttonTestLottie.setOnClickListener {
            startActivity(Intent(requireContext(), LottieAnimationActivity::class.java))
        }

        binding.buttonChipsDialog.setOnClickListener { showChipsDialog() }

        return binding.root
    }

    private fun showChipsDialog() {
        val dialogBinding = DialogSetTimeBinding.inflate(layoutInflater)
        val timeSlots = arrayListOf(
            TimeSlot("9:00 AM", false),
            TimeSlot("9:30 AM", true),
            TimeSlot("10:30 AM", true),
            TimeSlot("12:00 AM", false),
            TimeSlot("13:45 PM", false),
            TimeSlot("16:30 PM", true)
        )

        timeSlots.forEach { timeSlot ->
            val chip: Chip = (WidgetChoiseChipBinding.inflate(layoutInflater).root as Chip).apply {
                text = timeSlot.time
                isEnabled = timeSlot.enabled
            }
            dialogBinding.chipsTimeGroup.addView(chip)
        }

        val dialog = AlertDialog.Builder(requireContext(), R.style.Theme_CustomDialog)
            .setView(dialogBinding.root)
            .create()

        dialog.show()
    }

    inner class TimeSlot(val time: String, val enabled: Boolean)

}