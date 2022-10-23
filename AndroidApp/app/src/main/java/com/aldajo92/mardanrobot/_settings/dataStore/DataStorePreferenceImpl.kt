package com.aldajo92.mardanrobot._settings.dataStore

import android.content.Context
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class DataStorePreferenceImpl @Inject constructor(
    context: Context
) : DataStorePreference {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private val Context.dataStore by preferencesDataStore(name = "settings_data_store", scope = coroutineScope)

    private val dataSource = context.dataStore

    override fun <T> getPreference(key: Key<T>, defaultValue: T):
            Flow<T> = dataSource.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }

    override fun <T> putPreference(key: Key<T>, value: T) {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.edit { preferences ->
                preferences[key] = value
            }
        }
    }

    override suspend fun <T> removePreference(key: Key<T>) {
        dataSource.edit {
            it.remove(key)
        }
    }

    override suspend fun clearAllPreference() {
        dataSource.edit { preferences ->
            preferences.clear()
        }
    }

    override fun close(){
        coroutineScope.coroutineContext.job.cancel()
    }

}
