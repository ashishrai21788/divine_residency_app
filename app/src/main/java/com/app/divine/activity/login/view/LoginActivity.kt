package com.app.divine.activity.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.app.divine.R
import com.app.divine.databinding.ActivityLoginBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.extensions.clickWithDebounce
import com.app.core.extensions.showToastS
import com.app.core.extensions.validateEmail
import com.app.divine.AppApplication
import com.app.divine.activity.forgot.view.ForgotPasswordActivity
import com.app.divine.navigation.VillaRoleRouter
import com.app.divine.activity.language.view.LanguageSelectionActivity
import com.app.divine.activity.login.di.DaggerLoginActivityComponent
import com.app.divine.activity.login.di.LoginActivityModule
import com.app.divine.activity.login.viewmodel.LoginViewModel
import com.app.divine.api.ApiResult
import com.app.divine.activity.signup.view.SignupActivity
import com.app.divine.utils.LanguageManager
import com.app.divine.utils.setTitleFromLanguage
import com.app.divine.utils.BaseLanguageActivity
import com.app.core.dagger.qualifier.DefaultRetrofit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class LoginActivity : BaseLanguageActivity() {

    @Inject
    lateinit var viewModel: LoginViewModel

    @Inject
    lateinit var appDatabase: AppDatabase

    lateinit var binding: ActivityLoginBinding

    @Inject
    @field:DefaultRetrofit
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data?.getStringExtra("resultKey")
            Toast.makeText(this, "Result: $data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        DaggerLoginActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .loginActivityModule(LoginActivityModule(this))
            .build()
            .inject(this)

//        val result = appDatabase.userDao().getAll()
//        Log.e(TAG, "onCreate: ${result.size}")
//        appPreferences.setAllPreferenceTypeString("test", "testing")
//        Log.e(TAG, "onCreate: ${appPreferences.getAllPreferenceTypeString("test")}")

        initializeViews()
        clickListener()
        observeLoginResult()
    }
    
    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_language_selection, menu)
        return true
    }

    private fun observeLoginResult() {
        viewModel.villaLoginResult.observe(this) { result ->
            when (result) {
                is ApiResult.Loading -> { /* optional: show progress */ }
                is ApiResult.Success -> {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                    (application as? AppApplication)?.sendFcmTokenToBackendIfLoggedIn()
                    startActivity(VillaRoleRouter.getPostLoginIntent(this, appPreferences))
                    finish()
                }
                is ApiResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clickListener() {
        binding.tvForgotPassword.clickWithDebounce {
            launcher.launch(Intent(this, ForgotPasswordActivity::class.java))
//            launcher.launch(Intent(this, LandingMainActivity::class.java))
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Add language selection option to toolbar menu
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_language -> {
                    launcher.launch(Intent(this, LanguageSelectionActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.tvCreateAccount.clickWithDebounce {
            launcher.launch(Intent(this, SignupActivity::class.java))
        }

        binding.btnSignIn.clickWithDebounce {
            if (isLoginValidated()) {
                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()
                callLoginApi(email, password)
            } else {
                Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeViews() {
        updateUIForLanguageChange()
    }
    
    override fun updateUIForLanguageChange() {
        runOnUiThread {
            // Set toolbar title from language file
            binding.toolbar.setTitleFromLanguage("login.title")
            
            // Set text from language file
            binding.headline = LanguageManager.getString("login.title")
            binding.subheadline = "Welcome back you have been missed!"
            binding.email = LanguageManager.getString("login.email")
            binding.password = LanguageManager.getString("login.password")
            binding.emailHint = "Enter your email"
            binding.passwordHint = "Enter your password"
            binding.forgotPasswordText = LanguageManager.getString("login.forgot_password")
            binding.signInText = LanguageManager.getString("login.login_button")
            binding.createAccountText = LanguageManager.getString("login.signup_link")
            binding.orContinueWithText = "Or Continue With"
            binding.appBarTitle = LanguageManager.getString("login.title")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
    }

    private fun LoginActivity.callLoginApi(email: String, password: String) {
        viewModel.callLoginApi(email, password)
    }
}


private fun LoginActivity.isLoginValidated(): Boolean {
    if (binding.etEmail.text.toString().isEmpty() || !binding.etEmail.text.toString()
            .validateEmail()
    ) {
        showToastS("Enter valid Email-id")
        return false
    } else if (binding.etPassword.text.toString().length < 8 && binding.etPassword.text.toString()
            .isEmpty()
    ) {
        showToastS("Enter valid password")
        return false
    }
    return true
}
