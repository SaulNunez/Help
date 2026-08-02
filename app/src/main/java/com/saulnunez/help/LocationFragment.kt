package com.saulnunez.help

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.color.MaterialColors
import com.saulnunez.help.databinding.LocationFragmentBinding

class LocationFragment: Fragment(R.layout.location_fragment) {
    private var _binding: LocationFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: HelpRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LocationFragmentBinding.inflate(inflater, container, false)
        repository = HelpRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refreshUI()

        binding.cardLocation.setOnClickListener {
            repository.isLocationEnabled = !repository.isLocationEnabled
            toggleLocationService(repository.isLocationEnabled)
            refreshUI()
        }
    }

    private fun refreshUI() {
        updateTileUI(
            binding.cardLocation,
            binding.iconLocation,
            binding.statusLocation,
            repository.isLocationEnabled,
            R.drawable.ic_sharp_location_on_24px,
            R.drawable.ic_sharp_location_off_24px,
        )
    }

    private fun updateTileUI(
        card: MaterialCardView,
        iconView: ImageView,
        statusText: TextView,
        isChecked: Boolean,
        @DrawableRes activeIcon: Int,
        @DrawableRes inactiveIcon: Int
    ) {
        if (isChecked) {
            val primaryContainer = MaterialColors.getColor(card, com.google.android.material.R.attr.colorPrimaryContainer)
            val onPrimaryContainer = MaterialColors.getColor(card, com.google.android.material.R.attr.colorOnPrimaryContainer)
            
            card.setCardBackgroundColor(ColorStateList.valueOf(primaryContainer))
            iconView.setImageResource(activeIcon)
            iconView.imageTintList = ColorStateList.valueOf(onPrimaryContainer)
            statusText.text = getString(R.string.active)
            statusText.setTextColor(onPrimaryContainer)
        } else {
            val surface = MaterialColors.getColor(card, com.google.android.material.R.attr.colorSurfaceVariant)
            val onSurfaceVariant = MaterialColors.getColor(card, com.google.android.material.R.attr.colorOnSurfaceVariant)
            
            card.setCardBackgroundColor(ColorStateList.valueOf(surface))
            iconView.setImageResource(inactiveIcon)
            iconView.imageTintList = ColorStateList.valueOf(onSurfaceVariant)
            statusText.text = getString(R.string.inactive)
            statusText.setTextColor(onSurfaceVariant)
        }
    }

    private fun toggleLocationService(enabled: Boolean) {
        val intent = Intent(requireActivity(), HelpLocationService::class.java)
        if (enabled) {
            requireActivity().startService(intent)
        } else {
            requireActivity().stopService(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}