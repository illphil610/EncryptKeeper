package com.newwesterndev.encrypt_keeper

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cursor: Cursor = contentResolver.query(Uri.parse("content://com.newwestern.dev.provider.ENCRYPT_KEYS"),
                null,
                null,
                null,
                null)
        cursor.moveToNext()
        Log.e("TEST_KEY", cursor.getString(1))
    }
}
