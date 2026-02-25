package com.app.divine.activity.landing.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.activity.landing.view.LandingMainActivity
import dagger.Component

@LandingMainActivityScope
@Component(dependencies = [CoreComponent::class], modules = [LandingMainActivityModule::class])
interface LandingMainActivityComponent {
    fun inject(activity: LandingMainActivity)
}