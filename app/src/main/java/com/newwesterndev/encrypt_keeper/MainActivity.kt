package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.newwesterndev.encrypt_keeper.Model.Model
import com.newwesterndev.encrypt_keeper.Utilities.EncryptDelegate
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import kotlinx.android.synthetic.main.activity_main.*
import java.security.KeyPair

class MainActivity : Activity() {

    private lateinit var encryptedText: ByteArray
    private var encryptDelegate: EncryptDelegate = RSAEncryptUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create my utility object and Content Resolver and get a Cursor object to query data
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()

        // Extract the strings (keys in type String) from my Content Provider
        val publicKeyAsString = mCursor.getString(publicKey)
        val privateKeyAsString = mCursor.getString(privateKey)

        // this is converting the String being passed by the cursor object into Private and Public keys
        // so I can use Cipher to do the encryption type of fancy stuff and more neat things....
        val generatedKeyPair = KeyPair(encryptDelegate.getPublicKeyFromString(publicKeyAsString),
                encryptDelegate.getPrivateKeyFromString(privateKeyAsString))
        val providerKeys = Model.ProviderKeys(generatedKeyPair, publicKeyAsString, privateKeyAsString)

        encryptButton.setOnClickListener {
            if (!TextUtils.isEmpty(encryptEditText.text)) {
                encryptedText = encryptDelegate.encrypt(encryptEditText.text.toString(), providerKeys.keys.public)
                displayTextEncryption.text = encryptedText.toString()
                decryptButton.isEnabled = true
                encryptButton.isEnabled = false
            } else {
                if (!TextUtils.isEmpty(displayTextEncryption.text)) {
                    encryptedText = encryptDelegate.encrypt(displayTextEncryption.text.toString(), providerKeys.keys.public)
                    displayTextEncryption.text = encryptedText.toString()
                    decryptButton.isEnabled = true
                    encryptButton.isEnabled = false
                }
            }
        }

        decryptButton.setOnClickListener {
            if (!TextUtils.isEmpty(displayTextEncryption.text)) {
                val decryptedText = encryptedText.let { textToDecrypt -> encryptDelegate.decrypt(textToDecrypt, providerKeys.keys.private) }
                displayTextEncryption.text = decryptedText
                decryptButton.isEnabled = false
                encryptButton.isEnabled = true
            }
        }
    }

    companion object {
        private const val URI = "content://com.newwestern.dev.provider.ENCRYPT_KEYS"
        private const val publicKey = 0
        private const val privateKey = 1
    }
}
