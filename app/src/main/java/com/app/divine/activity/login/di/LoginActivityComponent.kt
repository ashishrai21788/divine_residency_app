package com.app.divine.activity.login.di


import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.login.view.LoginActivity
import dagger.Component

@LoginActivityScope
@Component(dependencies = [CoreComponent::class]
    , modules = [LoginActivityModule::class]
)
interface LoginActivityComponent {
    fun inject(activity: LoginActivity)
}