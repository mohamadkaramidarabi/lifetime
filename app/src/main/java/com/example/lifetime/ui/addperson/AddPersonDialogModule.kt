package com.example.lifetime.ui.addperson

import dagger.Module
import dagger.Provides

@Module
class AddPersonDialogModule {


    @Provides
    internal fun providePresenter(addPersonPresenter: AddPersonPresenter<AddPersonInteractor.AddPersonMVPDialog>):
            AddPersonInteractor.AddPersonMVPPresenter<AddPersonInteractor.AddPersonMVPDialog> = addPersonPresenter
}