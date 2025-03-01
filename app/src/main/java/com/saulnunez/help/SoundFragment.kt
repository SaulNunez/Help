package com.saulnunez.help

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.saulnunez.help.databinding.SoundFragmentBinding

class SoundFragment: Fragment(R.layout.sound_fragment) {
    private var _binding: SoundFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SoundFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchAudio.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val serviceIntent = Intent(this.activity, HelpSoundAlarmService::class.java)
                requireActivity().startService(serviceIntent)
            } else {
                val stopIntent = Intent(this.activity, HelpSoundAlarmService::class.java)
                requireActivity().stopService(stopIntent)
            }
        }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                val serviceIntent = Intent(this.activity, HelpLocationService::class.java)
                requireActivity().startService(serviceIntent)
            } else {
                val stopIntent = Intent(this.activity, HelpLocationService::class.java)
                requireActivity().stopService(stopIntent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}