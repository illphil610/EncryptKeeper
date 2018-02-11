package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import com.newwesterndev.encrypt_keeper.Model.Model
import com.newwesterndev.encrypt_keeper.Utilities.EncryptDelegate
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var encryptedText: ByteArray
    private lateinit var providerKeys: Model.ProviderKeys
    private var encryptDelegate: EncryptDelegate = RSAEncryptUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Create a Content Resolver and get a Cursor object to query data
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()

        requestKeyPairButton.setOnClickListener {
            providerKeys = encryptDelegate.requestKeyPair(mCursor, encryptDelegate)
            encryptDelegate.showToast("KeyPair has been generated!", this)
            encryptButton.isEnabled = true
        }

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
                } else {
                    encryptDelegate.showToast("Please enter text to be encrypted", this)
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
    }
}
