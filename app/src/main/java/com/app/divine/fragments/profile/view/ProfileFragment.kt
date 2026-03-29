package com.app.divine.fragments.profile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.app.divine.AppApplication
import com.app.divine.api.ApiResult
import com.app.divine.api.dataOrNull
import com.app.divine.databinding.ProfileFragmentBinding
import com.app.divine.activity.login.view.LoginActivity
import com.app.divine.api.villa.VillaAuthApi
import com.app.divine.repository.VillaAuthRepository
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.ui.coreComponent
import com.app.core.R
import com.app.divine.fragments.profile.di.DaggerProfileFragmentComponent
import com.app.divine.fragments.profile.di.ProfileFragmentModule
import com.app.core.dagger.qualifier.DefaultRetrofit
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class ProfileFragment : Fragment() {

    private lateinit var binding: ProfileFragmentBinding

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    @field:DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener { performLogout() }
        binding.btnChangePassword.setOnClickListener { showChangePasswordDialog() }
        loadProfile()
    }

    private fun loadProfile() {
        val api = coreComponent().villaSocietyRetrofit().create(VillaAuthApi::class.java)
        val repo = VillaAuthRepository(api, appPreferences)
        repo.getProfile { result ->
            if (result is ApiResult.Success) {
                val user = result.dataOrNull()
                val displayName = user?.name?.ifBlank { null } ?: user?.email ?: user?.username ?: "—"
                binding.profileName.text = displayName
                binding.profileAvatarInitial.text =
                    displayName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                binding.profileEmail.text = user?.email?.takeIf { !it.isNullOrBlank() } ?: "—"
                binding.profilePhone.text = user?.mobile?.takeIf { !it.isNullOrBlank() } ?: "—"
            }
        }
    }

    private fun showChangePasswordDialog() {
        val current = EditText(requireContext()).apply {
            hint = "Current password"
            setPadding(48, 32, 48, 32)
        }
        val newPass = EditText(requireContext()).apply {
            hint = "New password"
            setPadding(48, 32, 48, 32)
        }
        val layout = android.widget.LinearLayout(requireContext()).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            addView(current)
            addView(newPass)
        }
        AlertDialog.Builder(requireContext(), R.style.AppCompatAlertDialogStyle)
            .setTitle("Change password")
            .setView(layout)
            .setPositiveButton("Change") { _, _ ->
                val cur = current.text?.toString()?.trim() ?: ""
                val newP = newPass.text?.toString()?.trim() ?: ""
                if (cur.isBlank() || newP.isBlank()) {
                    Snackbar.make(binding.root, "Fill both fields", Snackbar.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                val api = coreComponent().villaSocietyRetrofit().create(VillaAuthApi::class.java)
                VillaAuthRepository(api, appPreferences).changePassword(cur, newP) { res ->
                    when (res) {
                        is ApiResult.Success -> Snackbar.make(binding.root, "Password updated", Snackbar.LENGTH_SHORT).show()
                        is ApiResult.Error -> Snackbar.make(binding.root, res.message, Snackbar.LENGTH_SHORT).show()
                        else -> {}
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerProfileFragmentComponent.builder()
            .coreComponent(coreComponent())
            .profileFragmentModule(ProfileFragmentModule(this))
            .build()
            .inject(this)
    }

    private fun performLogout() {
        (activity?.application as? AppApplication)?.performLogout()
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        activity?.finish()
    }
} 