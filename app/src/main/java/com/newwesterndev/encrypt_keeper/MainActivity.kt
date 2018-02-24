package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.newwesterndev.encrypt_keeper.Model.Model
import com.newwesterndev.encrypt_keeper.Utilities.EncryptDelegate
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback{

    private lateinit var mNfcAdapter : NfcAdapter
    private lateinit var pemFile: String
    private lateinit var encryptedText: ByteArray
    private lateinit var providerKeys: Model.ProviderKeys
    private var encryptDelegate = RSAEncryptUtility()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (!mNfcAdapter.isEnabled) {
            encryptDelegate.showToast(getString(R.string.enableNFC), this)
        }
        mNfcAdapter.setNdefPushMessageCallback(this, this)
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this)

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

    override fun createNdefMessage(p0: NfcEvent?): NdefMessage {
        val mCursor = contentResolver.query(Uri.parse(URI), null, null, null, null)
        mCursor.moveToNext()
        mCursor.close()

        providerKeys = encryptDelegate.requestKeyPair(mCursor, encryptDelegate)
        pemFile = encryptDelegate.creatPEMObject(providerKeys.keys.public)

        val hello = "Hello World"
        val recordsToAttach = encryptDelegate.createNdefRecords(pemFile, hello)
        return NdefMessage(recordsToAttach)
    }

    override fun onNdefPushComplete(p0: NfcEvent?) {

        // Called when the system detects that our NdefMessage was successfully sent
        Log.e("NDEF COMPLETE", "Set a flag here to change so it doesnt send the key?")
    }

    override fun onResume() {
        super.onResume()
        val intent = intent
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val message = rawMessages[0] as NdefMessage
            val messageRecords = message.records
            displayTextEncryption.text = String(message.records[1].payload)
            encryptDelegate.showToast(String(message.records[0].payload), this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent?.action) {
            val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val message = rawMessages?.get(0) as NdefMessage
            val messageRecords = message.records
            displayTextEncryption.text = String(message.records[1].payload)
            encryptDelegate.showToast(String(message.records[0].payload), this)
        }
    }

    companion object {
        private const val URI = "content://com.newwestern.dev.provider.ENCRYPT_KEYS"
    }
}
