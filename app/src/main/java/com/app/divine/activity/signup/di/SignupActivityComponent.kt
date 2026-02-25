package com.app.divine.activity.signup.di


import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.signup.view.SignupActivity
import dagger.Component

@SignupActivityScope
@Component(dependencies = [CoreComponent::class]
    , modules = [SignupActivityModule::class]
)
interface SignupActivityComponent {
    fun inject(activity: SignupActivity)
}