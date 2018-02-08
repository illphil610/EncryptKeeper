package com.newwesterndev.encrypt_keeper.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Base64
import android.util.Log
import com.newwesterndev.encrypt_keeper.RSAEncryptUtility

class EncryptionContentProvider : ContentProvider() {

    val mUtility: RSAEncryptUtility = RSAEncryptUtility()

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

        // Create a KeyPair using created method
        val keyPair = mUtility.generateKey()
        val publicKeyToString = Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
        val privateKeyToString = Base64.encodeToString(keyPair.private.encoded, Base64.DEFAULT)

        // Add encoded keys to a cursor and show toast to use when finished
        val matrixCursor = MatrixCursor(arrayOf("public", "private"))
        matrixCursor.addRow(arrayOf(publicKeyToString, privateKeyToString))
        mUtility.showToast("KeyPair has been generated", context)
        return matrixCursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Not yet implemented")
    }
}
