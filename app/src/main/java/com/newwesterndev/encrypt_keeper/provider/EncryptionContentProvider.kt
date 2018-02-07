package com.newwesterndev.encrypt_keeper.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import java.security.KeyPair
import java.security.KeyPairGenerator

class EncryptionContentProvider : ContentProvider() {

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        // Implement this to handle requests to delete one or more rows.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun getType(uri: Uri): String? {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        // TODO: Implement this to handle requests to insert a new row.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate(): Boolean {
        // TODO: Implement this to initialize your content provider on startup.
        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val keyPair = getKeyPair()
        val matrixCursor = MatrixCursor(arrayOf("public", "private"))
        matrixCursor.addRow(arrayOf(keyPair?.public, keyPair?.private))
        return matrixCursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        // TODO: Implement this to handle requests to update one or more rows.
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
