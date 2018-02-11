package com.newwesterndev.encrypt_keeper.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class KeyDbHelper(context: Context) : ManagedSQLiteOpenHelper(context, "currentkeydb") {
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(KeySQLiteContract.TABLE_NAME, true,
                KeySQLiteContract.COLUMN_ID to INTEGER + PRIMARY_KEY,
                KeySQLiteContract.COLUMN_NAME to TEXT,
                KeySQLiteContract.COLUMN_PUBLIC to TEXT,
                KeySQLiteContract.COLUMN_PRIVATE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(KeySQLiteContract.TABLE_NAME, true)

    }

    companion object {
        private lateinit var instance: KeyDbHelper

        @Synchronized
        fun getInstance(context: Context) : KeyDbHelper {
            if (instance == null) {
                instance = KeyDbHelper(context.applicationContext)
            }
            return instance
        }
    }
}