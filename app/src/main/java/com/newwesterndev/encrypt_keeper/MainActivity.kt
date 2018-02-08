package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.newwesterndev.encrypt_keeper.Utilities.EncryptDelegate
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var encryptedText: ByteArray
    private var encryptDelegate: EncryptDelegate = RSAEncryptUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create my utility object and Content Resolver and get a Cursor object to query data
        //encryptDelegate = RSAEncryptUtility()
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()

        // Extract the strings (keys in type String) from my Content Provider
        val publicKey = mCursor.getString(publicKey)
        val privateKey = mCursor.getString(privateKey)

        // Will the Real PKI Keys please stand up.... (we're gonna have some encryption here)
        // this is converting the String being passed by the cursor object into Private and Public keys
        // so I can use Cipher to do the encryption type of fancy stuff and more neat things....
        val actualPublicKey = encryptDelegate.getPublicKeyFromString(publicKey)
        val actualPrivateKey = encryptDelegate.getPrivateKeyFromString(privateKey)

        encryptButton.setOnClickListener {
            val inputText = encryptEditText.text
            if (!TextUtils.isEmpty(inputText)) {
                encryptedText = encryptDelegate.encrypt(inputText.toString(), actualPublicKey)
                displayTextEncryption.text = encryptedText.toString()
                inputText.clear()
            } else {
                if (displayTextEncryption != null) {
                    encryptedText = encryptDelegate.encrypt(displayTextEncryption.text.toString(), actualPublicKey)
                    displayTextEncryption.text = encryptedText.toString()
                }
            }
            decryptButton.isEnabled = true
            encryptButton.isEnabled = false
        }

        decryptButton.setOnClickListener {
            if (displayTextEncryption != null) {
                val decryptedText = encryptedText.let { textToDecrypt -> encryptDelegate.decrypt(textToDecrypt, actualPrivateKey) }
                displayTextEncryption.text = decryptedText
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
