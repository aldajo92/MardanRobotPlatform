package com.aldajo92.mardanrobot._settings.model.visitor

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aldajo92.mardanrobot._settings.dataStore.DataStorePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

abstract class InputViewModel<T>(
    val title: String,
    val key: String,
    val defaultValue: T,
    private val dataStorePreference: DataStorePreference?
) {

    @Composable
    abstract fun VisitUI(context: Context, inputSettingsVisitor: InputSettingsVisitor)

    abstract fun getSettingValueFlow(): Flow<T>

    abstract fun updateSettingValue(it: T)

    fun closeDataStoreConnection() {
//        dataStorePreference?.
    }

}

class TitleSettingsViewModel(
    title: String,
    dataStorePreference: DataStorePreference? = null
) : InputViewModel<String>(title, "", "", dataStorePreference) {

    @Composable
    override fun VisitUI(context: Context, inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(context, this)
    }

    override fun getSettingValueFlow(): Flow<String> = flow { emit("") }

    override fun updateSettingValue(it: String) {

    }

}

class InputTextSettingsViewModel(
    title: String,
    key: String,
    defaultValue: String = "",
    dataStorePreference: DataStorePreference? = null
) : InputViewModel<String>(title, key, defaultValue, dataStorePreference) {

    @Composable
    override fun VisitUI(context: Context, inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(context, this)
    }

    override fun getSettingValueFlow(): Flow<String> = flow { emit("") }

    override fun updateSettingValue(it: String) {

    }

}

class ChoiceListSettingsViewModel(
    title: String,
    key: String,
    defaultValue: String = "",
    defaultList: List<String>,
    private val dataStorePreference: DataStorePreference? = null
) : InputViewModel<String>(title, key, defaultValue, dataStorePreference) {

    private val _preferenceKey = stringPreferencesKey(key)
    private val _preferenceValue = dataStorePreference?.getPreference(_preferenceKey, defaultValue)

    private val _preferenceListKey = stringPreferencesKey("${key}_list")
    private val _preferenceListValue =
        dataStorePreference?.getPreference(_preferenceListKey, defaultList.joinToString(","))?.map {
            it.split(",")
        }

    @Composable
    override fun VisitUI(context: Context, inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(context, this)
    }

    override fun getSettingValueFlow(): Flow<String> = _preferenceValue ?: flowOf("")

    override fun updateSettingValue(it: String) {
        dataStorePreference?.putPreference(_preferenceKey, it)
    }

    fun updateList(items: List<String>) {
        dataStorePreference?.putPreference(_preferenceListKey, items.joinToString(","))
    }

    fun getListValueFlow(): Flow<List<String>> = _preferenceListValue ?: flowOf(listOf())

}

class CheckSettingsViewModel(
    title: String,
    key: String,
    defaultValue: Boolean = false,
    private val dataStorePreference: DataStorePreference? = null
) : InputViewModel<Boolean>(title, key, defaultValue, dataStorePreference) {

    private val _preferenceKey = booleanPreferencesKey(key)
    private val _preferenceValue = dataStorePreference?.getPreference(_preferenceKey, defaultValue)

    @Composable
    override fun VisitUI(context: Context, inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(context, this)
    }

    override fun getSettingValueFlow(): Flow<Boolean> = _preferenceValue ?: flowOf(defaultValue)

    override fun updateSettingValue(it: Boolean) {
        dataStorePreference?.putPreference(_preferenceKey, it)
    }

}
