package com.app.divine.activity.forgot.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.divine.R
import com.app.divine.databinding.ActivityForgotPasswordBinding
import com.app.divine.activity.forgot.di.DaggerForgotPasswordActivityComponent
import com.app.divine.activity.forgot.di.ForgotPasswordActivityModule
import com.app.divine.AppApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase

class ForgotPasswordActivity : AppCompatActivity() {

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    @field:com.app.core.dagger.qualifier.DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        DaggerForgotPasswordActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .forgotPasswordActivityModule(ForgotPasswordActivityModule(this))
            .build()
            .inject(this)


        initViews()
    }

    private fun initViews() {
        binding.appBarTitle = "Forgot password"
        binding.instructionText =
            "Enter the email linked to your account. We will send you a link to reset your password."
        binding.emailHint = "Email address"
        binding.submitText = "Send reset link"
        binding.email = ""
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

}