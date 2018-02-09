package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
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
            if (!TextUtils.isEmpty(encryptEditText.text)) {
                encryptedText = encryptDelegate.encrypt(encryptEditText.text.toString(), actualPublicKey)
                displayTextEncryption.text = encryptedText.toString()
                inputText.clear()

                decryptButton.isEnabled = true
                encryptButton.isEnabled = false
            } else {
                if (!TextUtils.isEmpty(displayTextEncryption.text)) {
                    encryptedText = encryptDelegate.encrypt(displayTextEncryption.text.toString(), actualPublicKey)
                    displayTextEncryption.text = encryptedText.toString()

                    decryptButton.isEnabled = true
                    //decryptButton.background.setTint(resources.getColor(R.color.killa_purp))
                    //decryptButton.setTextColor(Color.parseColor("white"))
                    // turn d button to purple with white text

                    encryptButton.isEnabled = false
                    //encryptButton.background.setTint(Color.parseColor(R.color.mint_green.toString()))
                    //encryptButton.setTextColor(resources.getColor(R.color.mint_green))
                    // turn e button green with black font
                }
            }
        }

        decryptButton.setOnClickListener {
            if (!TextUtils.isEmpty(displayTextEncryption.text)) {
                val decryptedText = encryptedText.let { textToDecrypt -> encryptDelegate.decrypt(textToDecrypt, actualPrivateKey) }
                displayTextEncryption.text = decryptedText

                decryptButton.isEnabled = false
                //decryptButton.setTextColor(Color.parseColor("white"))
                //decryptButton.background.setTint(Color.parseColor("#8c9eff"))

                encryptButton.isEnabled = true
                //encryptButton.setTextColor(Color.parseColor("black"))
                //encryptButton.background.setTint(Color.parseColor("#a7ffeb"))
            }

        }
    }

    companion object {
        private const val URI = "content://com.newwestern.dev.provider.ENCRYPT_KEYS"
        private const val publicKey = 0
        private const val privateKey = 1
    }
}
