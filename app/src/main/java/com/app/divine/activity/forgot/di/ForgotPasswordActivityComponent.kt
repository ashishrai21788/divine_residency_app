package com.app.divine.activity.forgot.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.forgot.view.ForgotPasswordActivity
import dagger.Component

@ForgotPasswordActivityScope
@Component(dependencies = [CoreComponent::class], modules = [ForgotPasswordActivityModule::class])
interface ForgotPasswordActivityComponent {
    fun inject(activity: ForgotPasswordActivity)
}