package com.huyhieu.mydocuments.hilt.module

import com.huyhieu.mydocuments.ui.activities.main.MainVM
import com.huyhieu.mydocuments.ui.fragments.components.steps.StepsVM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
object SharedVMModule {

    @ActivityRetainedScoped
    @Provides
    fun provideMainVM() = MainVM()

    @ActivityRetainedScoped
    @Provides
    fun provideStepsVM() = StepsVM()
}
