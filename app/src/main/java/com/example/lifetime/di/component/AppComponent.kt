package com.example.lifetime.di.component

import android.app.Application
import com.example.lifetime.BaseApplication
import com.example.lifetime.di.builder.ActivityBuilder
import com.example.lifetime.di.builder.DialogBuilder
import com.example.lifetime.di.builder.FragmentBuilder
import com.example.lifetime.di.module.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(
    modules = [AndroidSupportInjectionModule::class, AppModule::class,
        ActivityBuilder::class]
)
@Singleton
interface AppComponent : AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}