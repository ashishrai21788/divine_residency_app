package com.app.divine.activity.guard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.divine.databinding.FragmentGuardAttendanceBinding

/** GuardGraph: Attendance tab — check-in / check-out. */
class GuardAttendanceFragment : Fragment() {
    private lateinit var binding: FragmentGuardAttendanceBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGuardAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }
}
