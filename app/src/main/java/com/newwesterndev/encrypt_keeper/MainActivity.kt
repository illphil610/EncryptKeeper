package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.security.*

class MainActivity : Activity() {

    private var encryptedText: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create my utility object and Content Resolver and get a Cursor object to query data
        val mUtility = RSAEncryptUtility()
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()

        // Extract the strings (keys in type String) from my Content Provider
        val publicKey = mCursor.getString(publicKey)
        val privateKey = mCursor.getString(privateKey)

        // Will the Real Keys please stand up.... this is converting the String being passed by
        // the cursor object into Private and Public keys so I can use Cipher to do the encryption
        val actualPublicKey = mUtility.getPublicKeyFromString(publicKey)
        val actualPrivateKey = mUtility.getPrivateKeyFromString(privateKey)

        encryptButton.setOnClickListener {
            val inputText = encryptEditText.text
            if (!TextUtils.isEmpty(inputText)) {
                encryptedText = mUtility.encrypt(inputText.toString(), actualPublicKey)
                displayTextEncryption.text = encryptedText.toString()
                inputText.clear()
            } else {
                if (displayTextEncryption != null) {
                    encryptedText = mUtility.encrypt(displayTextEncryption.text.toString(), actualPublicKey)
                    displayTextEncryption.text = encryptedText.toString()
                }
            }
            decryptButton.isEnabled = true
            encryptButton.isEnabled = false
        }

        decryptButton.setOnClickListener {
            if (displayTextEncryption != null) {
                val decryptedText = encryptedText?.let { textToDecrypt -> mUtility.decrypt(textToDecrypt, actualPrivateKey) }
                when {
                    decryptedText != null -> displayTextEncryption.text = decryptedText
                }
            }
            decryptButton.isEnabled = false
            encryptButton.isEnabled = true
        }
    }

    companion object {
        private const val URI = "content://com.newwestern.dev.provider.ENCRYPT_KEYS"
        private const val publicKey = 0
        private const val privateKey = 1
    }
}
