package com.newwesterndev.encrypt_keeper.Model

import java.security.KeyPair

/**
 * Created by Phil on 2/9/2018.
 */
object Model {
    data class ProviderKeys(val keys: KeyPair,
                           val publicKeyAsString: String,
                           val privateKeyAsString: String)
}