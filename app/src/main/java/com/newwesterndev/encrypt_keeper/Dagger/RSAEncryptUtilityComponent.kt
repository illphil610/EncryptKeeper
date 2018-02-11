package com.newwesterndev.encrypt_keeper.Dagger

import com.newwesterndev.encrypt_keeper.MainActivity
import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = arrayOf(RSAEncryptUtilityModule::class, AppModule::class))
interface RSAEncryptUtilityComponent {
    fun inject(activity: MainActivity)
}
