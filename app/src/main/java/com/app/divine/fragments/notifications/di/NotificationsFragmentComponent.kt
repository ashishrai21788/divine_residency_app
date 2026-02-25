package com.app.divine.fragments.notifications.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.fragments.notifications.view.NotificationsFragment
import dagger.Component

@NotificationsFragmentScope
@Component(dependencies = [CoreComponent::class], modules = [NotificationsFragmentModule::class])
interface NotificationsFragmentComponent {
    fun inject(fragment: NotificationsFragment)
} 