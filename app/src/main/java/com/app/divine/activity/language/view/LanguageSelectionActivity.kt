package com.app.divine.activity.language.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.divine.R
import com.app.divine.databinding.ActivityLanguageSelectionBinding
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.divine.AppApplication
import com.app.divine.activity.language.adapter.LanguageAdapter
import com.app.divine.activity.language.di.DaggerLanguageSelectionActivityComponent
import com.app.divine.activity.language.di.LanguageSelectionActivityModule
import com.app.divine.activity.language.model.LanguageItem
import com.app.divine.activity.language.viewmodel.LanguageSelectionViewModel
import com.app.divine.utils.LanguageHelper
import com.app.divine.utils.LanguageManager
import com.app.divine.utils.setTitleFromLanguage
import com.app.divine.utils.BaseLanguageActivity
import com.app.divine.utils.LanguageFlowManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

class LanguageSelectionActivity : BaseLanguageActivity() {
    //
    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var retrofit: Retrofit

    @Inject
    lateinit var okHttpClient: OkHttpClient

    @Inject
    lateinit var appPreferences: AppPreferences
    @Inject
    lateinit var viewModel: LanguageSelectionViewModel
    
    lateinit var binding: ActivityLanguageSelectionBinding
    private lateinit var languageAdapter: LanguageAdapter
    private var currentLanguageCode: String = ""
    private var isActivityFinishing: Boolean = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection)
        
        // Initialize Dagger
        DaggerLanguageSelectionActivityComponent.builder()
            .coreComponent((application as AppApplication).coreComponent)
            .languageSelectionActivityModule(LanguageSelectionActivityModule(this))
            .build()
            .inject(this)
        
        currentLanguageCode = LanguageManager.getCurrentLanguage()
        
        initializeViews()
        setupRecyclerView()
        observeViewModel()
    }
    
    private fun initializeViews() {
        // Set toolbar title from language file
        binding.toolbar.setTitleFromLanguage("settings.language")
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Set data binding variables based on whether it's first launch or not
        if (LanguageFlowManager.isFirstLaunch(this)) {
            binding.toolbarTitle = LanguageManager.getString("welcome.language_selection")
            binding.currentLanguage = LanguageManager.getString("welcome.title")
            binding.totalLanguages = LanguageManager.getString("welcome.select_language")
        } else {
            binding.toolbarTitle = LanguageManager.getString("settings.language")
            binding.currentLanguage = LanguageManager.getString("common.current") + ": " + 
                                     LanguageHelper.getCurrentLanguageDisplayName()
            binding.totalLanguages = LanguageManager.getString("common.total") + ": " + 
                                    LanguageManager.getAvailableLanguages(this).size
        }
        binding.continueText = LanguageManager.getString("common.continue")
        
        // Setup continue button
        binding.btnContinue.setOnClickListener {
            // Check if activity is still valid
            if (isFinishing || isDestroyed || isActivityFinishing) {
                return@setOnClickListener
            }
            
            isActivityFinishing = true
            
            if (LanguageFlowManager.isFirstLaunch(this)) {
                LanguageFlowManager.completeLanguageSelection(this)
                finish()
            } else {
                finish()
            }
        }
    }
    
    private fun setupRecyclerView() {
        languageAdapter = LanguageAdapter { languageItem ->
            onLanguageSelected(languageItem)
        }
        
        binding.recyclerViewLanguages.apply {
            layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            adapter = languageAdapter
        }
    }
    
    private fun observeViewModel() {
        viewModel.availableLanguages.observe(this) { languages ->
            val languageItems = languages.map { language ->
                LanguageItem(
                    code = language.code,
                    displayName = language.displayName,
                    nativeName = language.nativeName,
                    flag = LanguageHelper.getLanguageFlag(language.code),
                    isSelected = language.code == currentLanguageCode
                )
            }
            languageAdapter.submitList(languageItems)
        }
        
        viewModel.languageChanged.observe(this) { success ->
            // Check if activity is still valid
            if (isFinishing || isDestroyed || isActivityFinishing) {
                return@observe
            }
            
            if (success) {
                val message = LanguageManager.getString("common.success") + ": " + 
                             LanguageManager.getString("settings.language") + " " +
                             LanguageHelper.getCurrentLanguageDisplayName()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                
                // Update UI for new language
                updateUIForNewLanguage()
            } else {
                Toast.makeText(this, LanguageManager.getString("common.error"), Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun onLanguageSelected(languageItem: LanguageItem) {
        // Check if activity is still valid
        if (isFinishing || isDestroyed || isActivityFinishing) {
            android.util.Log.d("LanguageSelectionActivity", "Activity is finishing or destroyed, skipping language selection")
            return
        }
        
        if (languageItem.code != currentLanguageCode) {
            viewModel.changeLanguage(languageItem.code)
            currentLanguageCode = languageItem.code
            
            // Show success message
            Toast.makeText(this, 
                LanguageManager.getString("common.success") + ": " + 
                LanguageManager.getString("settings.language") + " " +
                languageItem.displayName, 
                Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun updateUIForLanguageChange() {
        // Check if activity is still valid
        if (isFinishing || isDestroyed || isActivityFinishing) {
            android.util.Log.d("LanguageSelectionActivity", "Activity is finishing or destroyed, skipping UI update")
            return
        }
        
        // Check if binding is initialized
        if (!::binding.isInitialized) {
            android.util.Log.d("LanguageSelectionActivity", "Binding not initialized, skipping UI update")
            return
        }
        
        runOnUiThreadSafely {
            try {
                // Double-check activity state inside UI thread
                if (isFinishing || isDestroyed) {
                    return@runOnUiThreadSafely
                }
                
                // Update toolbar title and data binding variables based on whether it's first launch or not
                if (LanguageFlowManager.isFirstLaunch(this)) {
                    binding.toolbar.setTitleFromLanguage("welcome.language_selection")
                    binding.toolbarTitle = LanguageManager.getString("welcome.language_selection")
                    binding.currentLanguage = LanguageManager.getString("welcome.title")
                    binding.totalLanguages = LanguageManager.getString("welcome.select_language")
                } else {
                    binding.toolbar.setTitleFromLanguage("settings.language")
                    binding.toolbarTitle = LanguageManager.getString("settings.language")
                    binding.currentLanguage = LanguageManager.getString("common.current") + ": " + 
                                             LanguageHelper.getCurrentLanguageDisplayName()
                    binding.totalLanguages = LanguageManager.getString("common.total") + ": " + 
                                            LanguageManager.getAvailableLanguages(this).size
                }
                binding.continueText = LanguageManager.getString("common.continue")
                
                // Refresh the language list to update selection
                viewModel.availableLanguages.value?.let { languages ->
                    val languageItems = languages.map { language ->
                        LanguageItem(
                            code = language.code,
                            displayName = language.displayName,
                            nativeName = language.nativeName,
                            flag = LanguageHelper.getLanguageFlag(language.code),
                            isSelected = language.code == currentLanguageCode
                        )
                    }
                    languageAdapter.submitList(languageItems)
                }
            } catch (e: Exception) {
                // Log the error but don't crash
                android.util.Log.e("LanguageSelection", "Error updating UI for language change: ${e.message}")
            }
        }
    }
    
    private fun updateUIForNewLanguage() {
        updateUIForLanguageChange()
    }
    
    override fun onDestroy() {
        isActivityFinishing = true
        super.onDestroy()
    }
    
    private fun restartApp() {
        // This is optional - you can restart the app to apply language changes
        // or just refresh the current activity
        val intent = Intent(this, AppApplication::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
} 