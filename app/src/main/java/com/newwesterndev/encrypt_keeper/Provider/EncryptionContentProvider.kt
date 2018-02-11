package com.newwesterndev.encrypt_keeper.Provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Base64
import com.newwesterndev.encrypt_keeper.Utilities.RSAEncryptUtility

class EncryptionContentProvider : ContentProvider() {

    private val mUtility: RSAEncryptUtility = RSAEncryptUtility()

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
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

        // Create a KeyPair using created method and convert to Base64 string
        // After keys have been generated, let the user know they have been created
        val keyPair = mUtility.generateKey()
        val publicKeyToString = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        val privateKeyToString = Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT)

        // Add encoded keys to a cursor and return
        val matrixCursor = MatrixCursor(arrayOf("public", "private"))
        matrixCursor.addRow(arrayOf(publicKeyToString, privateKeyToString))
        return matrixCursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }
}
