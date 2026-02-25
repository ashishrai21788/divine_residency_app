package com.app.divine.activity.signup.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.divine.R
import com.app.divine.databinding.ActivitySignupBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.divine.AppApplication
import com.app.divine.activity.loginSignup.view.LoginSignupActivity
import com.app.divine.activity.signup.di.DaggerSignupActivityComponent
import com.app.divine.activity.signup.di.SignupActivityModule
import com.app.divine.utils.BaseLanguageActivity
import com.app.divine.utils.LanguageManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class SignupActivity: BaseLanguageActivity() {

    val TAG =  SignupActivity::class.java.simpleName

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var binding: ActivitySignupBinding

    @Inject
    @field:com.app.core.dagger.qualifier.DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        DaggerSignupActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .signupActivityModule(SignupActivityModule(this))
            .build()
            .inject(this)

        val result  = appDatabase.userDao().getAll()
        Log.e(TAG, "onCreate: ${result.size}", )
        appPreferences.setAllPreferenceTypeString("test", "testing")
        Log.e(TAG, "onCreate: ${appPreferences.getAllPreferenceTypeString("test")}", )
//        launchLandingPage()
        initializeViews()
        clickListeners()
    }

    private fun clickListeners() {
        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    private fun initializeViews() {
        updateUIForLanguageChange()
    }
    
    override fun updateUIForLanguageChange() {
        runOnUiThread {
            binding.headline = LanguageManager.getString("signup.title")
            binding.subheadline = "Create an account so that you can explore all the features of this app"
            binding.email = LanguageManager.getString("signup.email")
            binding.password = LanguageManager.getString("signup.password")
            binding.confirmPassword = LanguageManager.getString("signup.confirm_password")
            binding.emailHint = "Enter your email"
            binding.passwordHint = "Enter your password"
            binding.confirmPasswordHint = "Confirm your password"
            binding.signUpText = LanguageManager.getString("signup.signup_button")
            binding.alreadyHaveAccountText = LanguageManager.getString("signup.login_link")
        }
    }

    private fun launchLandingPage(){
        startActivity(Intent(this, LoginSignupActivity::class.java))
        finish()
    }
    override fun onResume() {
        super.onResume()
    }

}