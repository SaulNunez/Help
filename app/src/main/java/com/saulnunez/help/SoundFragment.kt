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
    private lateinit var repository: HelpRepository


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SoundFragmentBinding.inflate(inflater, container, false)
        repository = HelpRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.switchAudio.isChecked = repository.isAlarmEnabled
        binding.switchLocation.isChecked = repository.isLocationEnabled

        binding.switchAudio.setOnCheckedChangeListener { _, isChecked ->
            repository.isAlarmEnabled = isChecked
            if(isChecked) {
                val serviceIntent = Intent(this.activity, HelpSoundAlarmService::class.java)
                requireActivity().startService(serviceIntent)
            } else {
                val stopIntent = Intent(this.activity, HelpSoundAlarmService::class.java)
                requireActivity().stopService(stopIntent)
            }
        }
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            repository.isLocationEnabled = isChecked
            if(isChecked) {
                val serviceIntent = Intent(this.activity, HelpLocationService::class.java)
                requireActivity().startService(serviceIntent)
            } else {
                val stopIntent = Intent(this.activity, HelpLocationService::class.java)
                requireActivity().stopService(stopIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        _binding?.let {
            it.switchAudio.isChecked = repository.isAlarmEnabled
            it.switchLocation.isChecked = repository.isLocationEnabled
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}