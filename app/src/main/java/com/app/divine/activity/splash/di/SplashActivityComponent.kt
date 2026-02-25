package com.app.divine.activity.splash.di


import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.splash.view.SplashActivity
import dagger.Component

@SplashActivityScope
@Component(dependencies = [CoreComponent::class]
    , modules = [SplashActivityModule::class]
)
interface SplashActivityComponent {
    fun inject(activity: SplashActivity)
}