package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.text.TextUtils
import com.newwesterndev.encrypt_keeper.Model.Model
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private lateinit var mNfcAdapter: NfcAdapter
    private lateinit var encryptedText: ByteArray
    private lateinit var providerKeys: Model.ProviderKeys
    private lateinit var mReceivedMessageToDecrypt: ByteArray
    private var encryptDelegate = RSAEncryptUtility()
    private var keyHasBeenSent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // NFC stuff and things
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (!mNfcAdapter.isEnabled) {
            encryptDelegate.showToast(getString(R.string.enableNFC), this)
        }
        mNfcAdapter.setNdefPushMessageCallback(this, this)
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this)

        // Content Provider stuff and things
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()
        providerKeys = encryptDelegate.requestKeyPair(mCursor, encryptDelegate)

        encryptButton.setOnClickListener {
            if (!TextUtils.isEmpty(encryptEditText.text)) {
                encryptedText = encryptDelegate.encryptPrivate(encryptEditText.text.toString(), providerKeys.keys.private)
                displayTextEncryption.text = encryptedText.toString()

            } else {
                if (!TextUtils.isEmpty(displayTextEncryption.text)) {
                    encryptedText = encryptDelegate.encryptPrivate(displayTextEncryption.text.toString(), providerKeys.keys.private)
                    displayTextEncryption.text = encryptedText.toString()
                } else {
                    encryptDelegate.showToast("Please enter text to be encrypted", this)
                }
            }
        }
    }

    override fun createNdefMessage(p0: NfcEvent?): NdefMessage {
        val textToSend: ByteArray = if (displayTextEncryption != null) {
            encryptedText } else { "Hello World".toByteArray() }
        val pemFile = encryptDelegate.createPEMObject(providerKeys.keys.public)
        val recordsToAttach = encryptDelegate.createNdefRecords(pemFile, textToSend)
        return NdefMessage(recordsToAttach)
        }

        override fun onNdefPushComplete(p0: NfcEvent?) {
            keyHasBeenSent = true
        }

        override fun onResume() {
            super.onResume()
            val intent = intent
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                handleIntent(intent)
            }
        }

        override fun onNewIntent(intent: Intent?) {
            super.onNewIntent(intent)
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
                handleIntent(intent)
            }
        }

        private fun handleIntent(intent: Intent?) {
            val rawMessages = intent?.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val message = rawMessages?.get(0) as NdefMessage
            mReceivedMessageToDecrypt = message.records[1].payload
            val key = String(message.records[0].payload)
            val formatKey = encryptDelegate.formatPemPublicKeyString(key)
            val publicKey = encryptDelegate.getPublicKeyFromString(formatKey)
            displayTextEncryption.text = encryptDelegate.decryptPublic(mReceivedMessageToDecrypt, publicKey)
        }

        companion object {
            private const val URI = "content://com.newwestern.dev.provider.ENCRYPT_KEYS"
        }
}
