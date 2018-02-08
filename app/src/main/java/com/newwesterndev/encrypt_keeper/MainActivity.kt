package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Byte.decode
import java.security.*
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class MainActivity : Activity() {

    var mCipherDecrypt: Cipher? = null
    var mKeyPair: KeyPair? = null

    init {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mUtility = RSAEncryptUtility()
        val mCursor = contentResolver.query(Uri.parse("content://com.newwestern.dev.provider.ENCRYPT_KEYS"),
                null,
                null,
                null,
                null)
        mCursor.moveToNext()
        //mCursor.close()

        var inputText = encryptEditText.text
        val publicKey = mCursor.getString(0)
        val privateKey = mCursor.getString(1)

        Log.e("Public key", publicKey)
        Log.e("Private key", privateKey)
        val actualPublicKey = mUtility.getPublicKeyFromString(publicKey)
        val actualPrivateKey = mUtility.getPrivateKeyFromString(privateKey)
        Log.e("Actual Public", actualPublicKey.toString())
        Log.e("Actual Private", actualPrivateKey.toString())

        encryptButton.setOnClickListener {
            //val mCipherEncrypt = Cipher.getInstance("RSA")
            //mCipherEncrypt.init(Cipher.ENCRYPT_MODE, publicKey)
            //val encryptedBytes = mCipherEncrypt.doFinal(inputText.toString().toByteArray()
        }
    }

    private fun convertStringToPublicKey(publicKey: String): PublicKey? {
        val data = Base64.decode(publicKey.toByteArray(), 0)
        val spec = X509EncodedKeySpec(data)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec)
    }

    private fun convertStringToPrivateKey(privateKey: String): PrivateKey? {
        val data = Base64.decode(privateKey.toByteArray(), 0)
        val spec = X509EncodedKeySpec(data)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePrivate(spec)
    }
}
