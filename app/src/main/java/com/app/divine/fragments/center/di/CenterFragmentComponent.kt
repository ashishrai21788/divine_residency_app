package com.app.divine.fragments.center.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.fragments.center.view.CenterFragment
import dagger.Component

@CenterFragmentScope
@Component(dependencies = [CoreComponent::class], modules = [CenterFragmentModule::class])
interface CenterFragmentComponent {
    fun inject(fragment: CenterFragment)
} 