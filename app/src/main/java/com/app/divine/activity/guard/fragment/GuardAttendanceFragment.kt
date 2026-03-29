package com.app.divine.activity.guard.fragment

import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.divine.R
import com.app.divine.api.ApiResult
import com.app.divine.databinding.FragmentGuardAttendanceBinding
import com.app.divine.villa.VillaViewModelFactory
import com.app.divine.villa.viewmodel.VillaGuardAttendanceViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** GuardGraph: Attendance tab — check-in / check-out. */
class GuardAttendanceFragment : Fragment() {

    private lateinit var binding: FragmentGuardAttendanceBinding
    private lateinit var viewModel: VillaGuardAttendanceViewModel

    override fun onCreateView(inflater: android.view.LayoutInflater, container: android.view.ViewGroup?, savedInstanceState: android.os.Bundle?): android.view.View {
        binding = FragmentGuardAttendanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaGuardAttendanceViewModel::class.java]

        binding.attendanceShiftTime.text =
            SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault()).format(Date())
        binding.attendanceStatus.text = "Check in or out to record attendance."
        binding.attendanceCheckIn.visibility = android.view.View.VISIBLE
        binding.attendanceCheckOut.visibility = android.view.View.VISIBLE
        binding.attendanceCheckIn.setOnClickListener { viewModel.checkIn(null) }
        binding.attendanceCheckOut.setOnClickListener { viewModel.checkOut(null) }
        binding.attendanceError.findViewById<Button>(R.id.error_retry)?.setOnClickListener { /* no reload */ }

        viewModel.checkInResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    Snackbar.make(binding.root, "Checked in", Snackbar.LENGTH_SHORT).show()
                    binding.attendanceStatus.text = "Checked in."
                }
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
        viewModel.checkOutResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    Snackbar.make(binding.root, "Checked out", Snackbar.LENGTH_SHORT).show()
                    binding.attendanceStatus.text = "Checked out."
                }
                is ApiResult.Error -> Snackbar.make(binding.root, result.message, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}
