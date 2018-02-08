package com.newwesterndev.encrypt_keeper.Utilities

import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey

/**
 * Created by Phil on 2/7/2018.
 */

interface EncryptDelegate {
    fun generateKey(): KeyPair
    fun encrypt(textToEncrypt: String, publicKey: PublicKey): ByteArray
    fun decrypt(textToDecrypt: ByteArray, privateKey: PrivateKey): String
    fun getPrivateKeyFromString(key: String): PrivateKey
    fun getPublicKeyFromString(key: String): PublicKey
}
