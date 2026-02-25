package com.app.divine.activity.loginSignup.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.divine.R
import com.app.divine.databinding.ActivityLoginSignupBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.clickWithDebounce
import com.app.divine.AppApplication
import com.app.divine.activity.login.view.LoginActivity
import com.app.divine.activity.loginSignup.di.DaggerLoginSignupActivityComponent
import com.app.divine.activity.loginSignup.di.LoginSignupActivityModule
import com.app.divine.activity.signup.view.SignupActivity
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class LoginSignupActivity : AppCompatActivity() {

    val TAG = LoginSignupActivity::class.java.simpleName

    private lateinit var binding: ActivityLoginSignupBinding

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_signup)
        DaggerLoginSignupActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .loginSignupActivityModule(LoginSignupActivityModule(this))
            .build()
            .inject(this)

        val result = appDatabase.userDao().getAll()
        Log.e(TAG, "onCreate: ${result.size}")
        appPreferences.setAllPreferenceTypeString("test", "testing")
        Log.e(TAG, "onCreate: ${appPreferences.getAllPreferenceTypeString("test")}")

        initLayout()
        initListener()


    }

    private fun initListener() {
        binding.btnLogin.clickWithDebounce {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnRegister.clickWithDebounce {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.toolbar.setNavigationOnClickListener {
            Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initLayout() {
        binding.headline = "Headline"
        binding.subtext = "Subtext"
        binding.loginText = "Login"
        binding.registerText = "Register"
        binding.appBarTitle = "Login/Signup"
    }

    override fun onResume() {
        super.onResume()
    }
} 