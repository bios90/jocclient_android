package com.justordercompany.client.local_data

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.Constants
import com.justordercompany.client.extensions.toJsonMy
import com.justordercompany.client.extensions.toObjOrNullGson
import com.justordercompany.client.logic.models.ModelUser
import java.lang.Exception
import java.lang.RuntimeException

@SuppressLint("ApplySharedPref")
class SharedPrefsManager
{
    enum class Key
    {
        FB_TOKEN,
        CURRENT_USER
    }

    companion object
    {
        fun saveBool(key: Key, value: Boolean)
        {
            val editor = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).edit()
            editor.putBoolean(key.name, value)
            editor.commit()
        }

        fun getBool(key: Key): Boolean
        {
            val prefs = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE)
            val value = prefs.getBoolean(key.name, false)
            return value
        }

        fun saveString(key: Key, value: String)
        {
            val editor = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).edit()
            editor.putString(key.name, value)
            editor.commit()
        }

        fun getString(key: Key): String?
        {
            val prefs = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE)
            val value = prefs.getString(key.name, null)
            return value
        }

        fun saveObj(key: Key, obj: Any)
        {
            val editor = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE).edit()
            val obj_as_str = obj.toJsonMy()
            if (obj_as_str == null)
            {
                throw RuntimeException("**** Error got null objs on saving ****")
            }
            editor.putString(key.name, obj_as_str)
            editor.commit()
        }

        fun <T> getObj(key: Key, obj_class: Class<T>): T?
        {
            try
            {
                val prefs = AppClass.app.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE)
                val str = prefs.getString(key.name, null)
                val obj = str?.toObjOrNullGson(obj_class)
                return obj
            }
            catch (e: Exception)
            {
                return null
            }
        }

        fun saveUser(user: ModelUser)
        {
            saveObj(Key.CURRENT_USER, user)
        }

        fun getCurrentUser(): ModelUser?
        {
            return getObj(Key.CURRENT_USER, ModelUser::class.java)
        }

        fun getUserToken(): String?
        {
            return getCurrentUser()?.api_token
        }
    }
}