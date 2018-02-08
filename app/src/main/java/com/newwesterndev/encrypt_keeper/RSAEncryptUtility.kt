package com.newwesterndev.encrypt_keeper

import android.content.Context

import android.util.Log

import android.widget.Toast
import org.spongycastle.util.encoders.Base64

import java.io.IOException
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

/**
 * Created by philip on 2/7/18.
 */
class RSAEncryptUtility {

    init {
        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
    }

    @Throws(NoSuchAlgorithmException::class)
    fun generateKey(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance(ALGORITHM)
        keyGen.initialize(1048)
        return keyGen.genKeyPair()
    }

    @Throws(Exception::class)
    fun encrypt(text: ByteArray, key: PublicKey): ByteArray? {
        val cipherText: ByteArray?
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        cipherText = cipher.doFinal(text)
        return cipherText
    }

    @Throws(Exception::class)
    fun getPrivateKeyFromString(key: String): PrivateKey {
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val privateKeySpec = PKCS8EncodedKeySpec(decodeBASE64(key))
        return keyFactory.generatePrivate(privateKeySpec)
    }

    @Throws(Exception::class)
    fun getPublicKeyFromString(key: String): PublicKey {
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val publicKeySpec = X509EncodedKeySpec(decodeBASE64(key))
        return keyFactory.generatePublic(publicKeySpec)
    }

    @Throws(IOException::class)
    private fun decodeBASE64(text: String): ByteArray {
        return Base64.decode(text)
    }

    fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        private const val ALGORITHM = "RSA"
    }
}