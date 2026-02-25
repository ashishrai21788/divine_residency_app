package com.app.divine.fragments.resident

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentResidentComplaintsBinding

/** ResidentGraph: Complaints tab — create and view complaints. */
class ResidentComplaintsFragment : Fragment() {
    private lateinit var binding: FragmentResidentComplaintsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResidentComplaintsBinding.inflate(inflater, container, false)
        return binding.root
    }
}
