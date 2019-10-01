package com.example.lifetime.ui.addperson

import com.example.lifetime.ui.addperson.interactor.AddPersonInteractor
import com.example.lifetime.ui.addperson.interactor.AddPersonMVPInteractor
import com.example.lifetime.ui.addperson.presenter.AddPersonMVPPresenter
import com.example.lifetime.ui.addperson.presenter.AddPersonPresenter
import com.example.lifetime.ui.addperson.view.AddPersonMVPDialog
import dagger.Module
import dagger.Provides

@Module
class AddPersonDialogModule {

    @Provides
    internal fun provideAddPersonInteractor(addPersonInteractor: AddPersonInteractor): AddPersonMVPInteractor =
        addPersonInteractor

    @Provides
    internal fun providePresenter(addPersonPresenter: AddPersonPresenter<AddPersonMVPDialog,AddPersonMVPInteractor>):
            AddPersonMVPPresenter<AddPersonMVPDialog,AddPersonMVPInteractor> = addPersonPresenter
}