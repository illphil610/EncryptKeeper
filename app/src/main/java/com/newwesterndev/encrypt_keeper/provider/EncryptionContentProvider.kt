package com.newwesterndev.encrypt_keeper.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import java.security.Key
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.util.*
import javax.crypto.Cipher

class EncryptionContentProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        // at the given URI.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        // Create a KeyPair using created method
        val keyPair = getKeyPair()

        //val mCipher = Cipher.getInstance("RSA")
        //mCipher.init(Cipher.WRAP_MODE, keyPair?.public)

        //val byteArrayFromPublicKey = mCipher.wrap(keyPair?.public)
        //val byteArrayFromPrivateKey = mCipher.wrap(keyPair?.private)

        val matrixCursor = MatrixCursor(arrayOf("public", "private"))
        //matrixCursor.addRow(arrayOf(byteArrayFromPublicKey, byteArrayFromPrivateKey))
        return matrixCursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun getKeyPair() : KeyPair? {
        var keyPair : KeyPair? = null
        try {
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(2048)
            keyPair = generator.genKeyPair()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return keyPair
    }
}
