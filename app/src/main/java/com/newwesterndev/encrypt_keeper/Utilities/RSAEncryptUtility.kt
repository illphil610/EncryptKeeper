package com.newwesterndev.encrypt_keeper.Utilities

import android.content.Context
import android.database.Cursor

import android.widget.Toast
import com.newwesterndev.encrypt_keeper.Model.Model
import org.spongycastle.util.encoders.Base64

import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class RSAEncryptUtility : EncryptDelegate {

    init {
        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    override fun generateKey(): KeyPair {
        val keyGen = KeyPairGenerator.getInstance(ALGORITHM)
        keyGen.initialize(1024)
        return keyGen.genKeyPair()
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    override fun encrypt(textToEncrypt: String, publicKey: PublicKey): ByteArray {
        val mCipherEncrypt = Cipher.getInstance(ALGORITHM)
        mCipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey)
        return mCipherEncrypt.doFinal(textToEncrypt.toByteArray())
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    override fun decrypt(textToDecrypt: ByteArray, privateKey: PrivateKey): String {
        val mCipherDecrypt = Cipher.getInstance(ALGORITHM)
        mCipherDecrypt.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedBytes = mCipherDecrypt.doFinal(textToDecrypt)
        return String(decryptedBytes)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    override fun getPrivateKeyFromString(key: String): PrivateKey {
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val privateKeySpec = PKCS8EncodedKeySpec(Base64.decode(key))
        return keyFactory.generatePrivate(privateKeySpec)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    override fun getPublicKeyFromString(key: String): PublicKey {
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val publicKeySpec = X509EncodedKeySpec(Base64.decode(key))
        return keyFactory.generatePublic(publicKeySpec)
    }

    override fun showToast(message: String, context: Context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun requestKeyPair(cursor: Cursor, encryptDelegate: EncryptDelegate) : Model.ProviderKeys {
        val publicKeyAsString = cursor.getString(publicKey)
        val privateKeyAsString = cursor.getString(privateKey)
        val generatedKeyPair = KeyPair(encryptDelegate.getPublicKeyFromString(publicKeyAsString),
                encryptDelegate.getPrivateKeyFromString(privateKeyAsString))
        return Model.ProviderKeys(generatedKeyPair, publicKeyAsString, privateKeyAsString)
    }

    companion object {
        private const val ALGORITHM = "RSA"
        private const val publicKey = 0
        private const val privateKey = 1
    }
}