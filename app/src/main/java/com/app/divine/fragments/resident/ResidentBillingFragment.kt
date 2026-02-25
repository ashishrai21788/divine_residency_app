package com.app.divine.fragments.resident

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentResidentBillingBinding

/** ResidentGraph: Billing tab — my bills and payments. */
class ResidentBillingFragment : Fragment() {
    private lateinit var binding: FragmentResidentBillingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentResidentBillingBinding.inflate(inflater, container, false)
        return binding.root
    }
}
