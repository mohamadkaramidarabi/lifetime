package com.example.lifetime.di.builder

import com.example.lifetime.ui.addperson.AddPersonDialogModule
import com.example.lifetime.ui.addperson.view.AddPersonDialog
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class DialogBuilder {

    @ContributesAndroidInjector(modules = [AddPersonDialogModule::class])
    abstract fun bindAddPersionDialog(): AddPersonDialog
}