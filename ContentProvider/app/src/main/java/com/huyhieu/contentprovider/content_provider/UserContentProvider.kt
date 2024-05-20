package com.huyhieu.contentprovider.content_provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.huyhieu.contentprovider.App
import com.huyhieu.contentprovider.room.entity.User
import kotlinx.coroutines.runBlocking

class UserContentProvider : ContentProvider() {

    companion object {
        private const val PACKAGE_NAME = "com.huyhieu.contentprovider"
        const val AUTHORITY = "$PACKAGE_NAME.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/users")

        const val USERS = 1
        const val USER_ID = 2
    }

    private val userDao by lazy { App.ins.db.userDao() }
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, "users", USERS)  // URI cho toàn bộ bảng users
        addURI(AUTHORITY, "users/#", USER_ID)  // URI cho một hàng cụ thể trong bảng users
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = when (uriMatcher.match(uri)) {
            USERS -> {
                val users = userDao.getAllUsersCP()
                MatrixCursor(arrayOf("id", "name", "age")).apply {
                    users.forEach {
                        this.addRow(arrayOf(it.id, it.name, it.age))
                    }
                }
            }

            USER_ID -> {
                val userId = ContentUris.parseId(uri)
                val user = userDao.getUserByIdCP(userId)
                MatrixCursor(arrayOf("id", "name", "age")).apply {
                    user?.let { addRow(arrayOf(user.id, user.name, user.age)) }
                }
            }

            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            USERS -> "vnd.android.cursor.dir/vnd.$PACKAGE_NAME.users"
            USER_ID -> "vnd.android.cursor.item/vnd.$PACKAGE_NAME.users"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (uriMatcher.match(uri) == USERS) {
            val name = values?.getAsString("name").orEmpty()
            val age = values?.getAsInteger("age") ?: 0
            val user = User(name = name, age = age)
            val id = runBlocking { userDao.insertUser(user) }
            context?.contentResolver?.notifyChange(uri, null)
            return ContentUris.withAppendedId(CONTENT_URI, id)
        }
        throw IllegalArgumentException("Invalid URI for delete operation: $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return when (uriMatcher.match(uri)) {
            USER_ID -> {
                val userId = ContentUris.parseId(uri)
                val rowsDeleted = runBlocking { userDao.deleteUserById(userId) }
                context?.contentResolver?.notifyChange(uri, null)
                rowsDeleted
            }

            else -> throw IllegalArgumentException("Invalid URI for delete operation: $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?
    ): Int {
        return when (uriMatcher.match(uri)) {
            USER_ID -> {
                val userId = ContentUris.parseId(uri)
                val name = values?.getAsString("name").orEmpty()
                val age = values?.getAsInteger("age") ?: 0
                val rowsUpdated = runBlocking { userDao.updateUser(userId, name, age) }
                context?.contentResolver?.notifyChange(uri, null)
                rowsUpdated
            }

            else -> throw IllegalArgumentException("Invalid URI for update operation: $uri")
        }
    }
}
