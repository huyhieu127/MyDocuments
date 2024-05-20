package com.huyhieu.mydocuments.ui.fragments.content_provider

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import com.huyhieu.mydocuments.base.BaseFragment
import com.huyhieu.mydocuments.databinding.FragmentContentProviderBinding
import com.huyhieu.mydocuments.models.User

const val TAG = "ContentProviderFragment"

class ContentProviderFragment : BaseFragment<FragmentContentProviderBinding>() {
    private val CONTENT_URI = Uri.parse("content://com.huyhieu.contentprovider.provider/users")
    private val adapter by lazy { UserCPAdapter() }
    private var posSelected = -1
    private var id = -1L

    override fun onMyViewCreated(savedInstanceState: Bundle?) {
        setClickViews(vb.btnInsert, vb.btnDelete)

        vb.rcvUsers.adapter = adapter
        adapter.onClickItemWithPosition = { pos, item ->
            posSelected = pos
            id = item.id
            Log.d(TAG, "User: $item")
        }
        loadUsers()
    }

    override fun FragmentContentProviderBinding.onClickViewBinding(v: View, id: Int) {
        when (v) {
            btnInsert -> {
                val age = (18..100).random()
                val user = User(name = "AutoName ${System.currentTimeMillis()}", age = age)
                val contentValues = ContentValues()
                contentValues.put("name", user.name)
                contentValues.put("age", user.age)
                val uri = requireContext().contentResolver.insert(CONTENT_URI, contentValues)
                Log.d(TAG, "URI: $uri")
                loadUsers()
                posSelected = -1
            }

            btnDelete -> {
                if (posSelected == -1) return
                val uri = ContentUris.withAppendedId(CONTENT_URI, this@ContentProviderFragment.id)
                val count = requireContext().contentResolver.delete(uri, null, null)
                Log.d(TAG, "Count: $count")
                adapter.removeField(posSelected)
                posSelected = -1
            }
        }
    }

    private fun loadUsers() {
        requireContext().contentResolver?.query(CONTENT_URI, null, null, null, null)?.use {
            val users = mutableListOf<User>()
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow("id"))
                val name = it.getString(it.getColumnIndexOrThrow("name"))
                val age = it.getInt(it.getColumnIndexOrThrow("age"))
                users.add(User(id = id, name = name, age = age))
            }
            it.close()
            adapter.fillData(users)
            adapter.notifyItemInserted(adapter.listData.lastIndex)
        }
    }
}
