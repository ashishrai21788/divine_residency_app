package com.app.divine.activity.loginSignup.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.loginSignup.view.LoginSignupActivity
import dagger.Component

@LoginSignupActivityScope
@Component(dependencies = [CoreComponent::class], modules = [LoginSignupActivityModule::class])
interface LoginSignupActivityComponent {
    fun inject(activity: LoginSignupActivity)
} 