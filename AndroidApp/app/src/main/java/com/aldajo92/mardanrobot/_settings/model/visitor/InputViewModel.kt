package com.aldajo92.mardanrobot._settings.model.visitor

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

abstract class InputViewModel<T>(
    val title: String,
    val key: String,
    val defaultValue: T
) {

    @Composable
    abstract fun VisitUI(inputSettingsVisitor: InputSettingsVisitor)

    abstract fun getSettingValueFlow(): Flow<T>

    abstract fun updateSettingValue(it: T)
}

class TitleSettingsViewModel(
    title: String,
) : InputViewModel<String>(title, "", "") {

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
    defaultValue: String = ""
) : InputViewModel<String>(title, key, defaultValue) {

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
    val listSelection : List<String>
) : InputViewModel<String>(title, key, defaultValue) {

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
    defaultValue: Boolean = false
) : InputViewModel<Boolean>(title, key, defaultValue) {

    private val _checkStatusFlow = MutableStateFlow(defaultValue)

    @Composable
    override fun VisitUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputSettingsVisitor.AcceptUI(this)
    }

    override fun getSettingValueFlow(): Flow<Boolean> = _checkStatusFlow

    override fun updateSettingValue(it: Boolean) {
        // TODO: Save to preferences here
        _checkStatusFlow.value = it
    }


}