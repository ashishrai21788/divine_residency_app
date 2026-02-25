package com.app.divine.fragments.home.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.fragments.home.view.HomeFragment
import dagger.Component

@HomeFragmentScope
@Component(dependencies = [CoreComponent::class], modules = [HomeFragmentModule::class])
interface HomeFragmentComponent {
    fun inject(fragment: HomeFragment)
} 