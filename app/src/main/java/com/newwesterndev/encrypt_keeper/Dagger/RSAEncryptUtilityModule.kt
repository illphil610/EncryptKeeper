package com.newwesterndev.encrypt_keeper.Dagger

import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RSAEncryptUtilityModule(){

    @Singleton
    @Provides
    fun providesEncryptDelegate(): RSAEncryptUtility{
        return RSAEncryptUtility()
    }
}