package com.newwesterndev.encrypt_keeper.Dagger

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val mApplication: Application){

    @Provides
    @Singleton
    fun provideApplication() : Application{
        return mApplication
    }
}