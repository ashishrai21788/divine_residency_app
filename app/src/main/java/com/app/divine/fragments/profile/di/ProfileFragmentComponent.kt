package com.app.divine.fragments.profile.di


import com.app.core.dagger.component.CoreComponent
import com.app.divine.fragments.profile.view.ProfileFragment
import dagger.Component

@ProfileFragmentScope
@Component(dependencies = [CoreComponent::class], modules = [ProfileFragmentModule::class])
interface ProfileFragmentComponent {
    fun inject(fragment: ProfileFragment)
} 