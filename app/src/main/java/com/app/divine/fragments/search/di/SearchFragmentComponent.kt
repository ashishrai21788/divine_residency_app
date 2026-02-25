package com.app.divine.fragments.search.di

import com.app.core.dagger.component.CoreComponent
import com.app.divine.fragments.search.view.SearchFragment
import dagger.Component

@SearchFragmentScope
@Component(dependencies = [CoreComponent::class], modules = [SearchFragmentModule::class])
interface SearchFragmentComponent {
    fun inject(fragment: SearchFragment)
} 