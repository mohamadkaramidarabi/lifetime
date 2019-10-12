package com.example.lifetime.di.builder

import com.example.lifetime.ui.addperson.AddPersonDialogModule
import com.example.lifetime.ui.addperson.AddPersonDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class DialogBuilder {

    @ContributesAndroidInjector(modules = [AddPersonDialogModule::class])
    abstract fun bindAddPersionDialog(): AddPersonDialog
}