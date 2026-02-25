package com.app.divine.activity.language.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.language.view.LanguageSelectionActivity
import dagger.Component

@LanguageSelectionActivityScope
@Component(dependencies = [CoreComponent::class], modules = [LanguageSelectionActivityModule::class])
interface LanguageSelectionActivityComponent {
    fun inject(activity: LanguageSelectionActivity)
} 