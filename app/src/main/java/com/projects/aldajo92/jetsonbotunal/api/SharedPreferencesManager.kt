package com.projects.aldajo92.jetsonbotunal.api

import android.content.Context
import androidx.preference.PreferenceManager
import com.projects.aldajo92.jetsonbotunal.KEY_SAMPLE_TIME
import com.projects.aldajo92.jetsonbotunal.KEY_URL
import com.projects.aldajo92.jetsonbotunal.KEY_URL_LOCAL_IP
import com.projects.aldajo92.jetsonbotunal.KEY_URL_REMOTE
import com.projects.aldajo92.jetsonbotunal.VALUE_URL_LOCAL_IP
import com.projects.aldajo92.jetsonbotunal.VALUE_URL_REMOTE

class SharedPreferencesManager(private val context: Context) {

    enum class IPConfigurations(val value: String) {
        REMOTE(KEY_URL_REMOTE), LOCAL_IP(KEY_URL_LOCAL_IP)
    }

    companion object {
        private val map = IPConfigurations.values().associateBy { it.value }
        fun fromValue(type: String) = map[type]
    }

    private val sharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveVideoSampleTime(time: Int) {
        saveKeyIntValue(KEY_SAMPLE_TIME, time)
    }

    fun getStoredVideoSampleTime() = sharedPreferences.getInt(KEY_SAMPLE_TIME, 500)

    fun saveIPConfiguration(ipConfigurations: IPConfigurations) {
        when (ipConfigurations) {
            IPConfigurations.REMOTE -> saveKeyValue(KEY_URL, KEY_URL_REMOTE)
            IPConfigurations.LOCAL_IP -> saveKeyValue(KEY_URL, KEY_URL_LOCAL_IP)
        }
    }

    fun getStoredIPConfiguration() =
        when (sharedPreferences.getString(KEY_URL, KEY_URL_LOCAL_IP) ?: KEY_URL_LOCAL_IP) {
            KEY_URL_REMOTE -> IPConfigurations.REMOTE
            else -> IPConfigurations.LOCAL_IP
        }

    fun getBaseUrl(ipConfigurations: IPConfigurations) =
        sharedPreferences.getString(
            ipConfigurations.value,
            when (ipConfigurations) {
                IPConfigurations.LOCAL_IP -> VALUE_URL_LOCAL_IP
                IPConfigurations.REMOTE -> VALUE_URL_REMOTE
            }
        )

    fun getSelectedBaseUrl(): String? {
        return getBaseUrl(getStoredIPConfiguration())
    }

    fun saveBaseURL(ipConfigurations: IPConfigurations, url: String) {
        when (ipConfigurations) {
            IPConfigurations.REMOTE -> saveKeyValue(KEY_URL_REMOTE, url)
            IPConfigurations.LOCAL_IP -> saveKeyValue(KEY_URL_LOCAL_IP, url)
        }
    }

    private fun saveKeyValue(key: String, value: String) {
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }

    private fun saveKeyIntValue(key: String, value: Int) {
        sharedPreferences.edit()
            .putInt(key, value)
            .apply()
    }

}
