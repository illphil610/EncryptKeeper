package com.newwesterndev.encrypt_keeper.Model

import java.security.KeyPair

object Model {
    data class ProviderKeys(val keys: KeyPair,
                            val publicKeyAsString: String,
                            val privateKeyAsString: String)
}