package com.app.divine.activity.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.activity.admin.view.AdminMainActivity
import com.app.divine.databinding.FragmentAdminHomeBinding

class AdminHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.adminHomeResidents.setOnClickListener {
            (activity as? AdminMainActivity)?.pushFragment(AdminResidentsFragment())
        }
    }
}
