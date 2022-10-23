package com.aldajo92.mardanrobot._settings.model.visitor

import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.aldajo92.mardanrobot._settings.dataStore.DataStorePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

abstract class InputViewModel<T>(
    val title: String,
    val key: String,
    val defaultValue: T,
    private val dataStorePreference: DataStorePreference?
) {

    @Composable
    abstract fun VisitUI(inputSettingsVisitor: InputSettingsVisitor)

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
    override fun VisitUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(this)
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
    override fun VisitUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(this)
    }

    override fun getSettingValueFlow(): Flow<String> = flow { emit("") }

    override fun updateSettingValue(it: String) {

    }

}

class ChoiceListSettingsViewModel(
    title: String,
    key: String,
    defaultValue: String = "",
    val listSelection: List<String>,
    dataStorePreference: DataStorePreference? = null
) : InputViewModel<String>(title, key, defaultValue, dataStorePreference) {

    @Composable
    override fun VisitUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(this)
    }

    override fun getSettingValueFlow(): Flow<String> = flow { emit("") }

    override fun updateSettingValue(it: String) {

    }

}

class CheckSettingsViewModel(
    title: String,
    key: String,
    defaultValue: Boolean = false,
    private val dataStorePreference: DataStorePreference? = null
) : InputViewModel<Boolean>(title, key, defaultValue, dataStorePreference) {

    private val _preferenceKey = booleanPreferencesKey(key)

    private val _preferenceValue: Flow<Boolean>? =
        dataStorePreference?.getPreference(_preferenceKey, defaultValue)

    @Composable
    override fun VisitUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(this)
    }

    override fun getSettingValueFlow(): Flow<Boolean> = _preferenceValue ?: flowOf(defaultValue)

    override fun updateSettingValue(it: Boolean) {
        dataStorePreference?.putPreference(_preferenceKey, it)
    }

}
