package com.huyhieu.mydocuments.hilt

import com.huyhieu.mydocuments.ui.activities.main.MainVM
import com.huyhieu.mydocuments.ui.fragments.steps.StepsVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Singleton


@Module
@InstallIn(ActivityRetainedComponent::class)
object SharedViewModelModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMainVM() = MainVM()

    @ActivityRetainedScoped
    @Provides
    fun provideStepsVM() = StepsVM()
}
