package com.aldajo92.mardanrobot._settings.model.visitor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aldajo92.mardanrobot._settings.ItemSettingsCheck
import com.aldajo92.mardanrobot._settings.ItemSettingsChoiceList
import com.aldajo92.mardanrobot._settings.ItemSettingsInputText
import com.aldajo92.mardanrobot._settings.ItemSettingsTitle

interface InputSettingsVisitor {

    @Composable
    fun AcceptUI(viewModel: InputTextSettingsViewModel)

    @Composable
    fun AcceptUI(viewModel: ChoiceListSettingsViewModel)

    @Composable
    fun AcceptUI(viewModel: CheckSettingsViewModel)

    @Composable
    fun AcceptUI(viewModel: TitleSettingsViewModel)

}

class InputSettingsVisitorImpl : InputSettingsVisitor {

    @Composable
    override fun AcceptUI(viewModel: InputTextSettingsViewModel) {
        ItemSettingsInputText()
    }

    @Composable
    override fun AcceptUI(viewModel: ChoiceListSettingsViewModel) {
        val selectedValue by viewModel.getSettingValueFlow().collectAsState(null)
        ItemSettingsChoiceList(
            text = viewModel.title,
            values = viewModel.listSelection,
            selectedValue = selectedValue
        ){
            viewModel.updateSettingValue(it)
        }
    }

    @Composable
    override fun AcceptUI(viewModel: CheckSettingsViewModel) {
        val checkState by viewModel.getSettingValueFlow().collectAsState(false)
        ItemSettingsCheck(
            text = viewModel.title,
            checkState = checkState
        ) {
            viewModel.updateSettingValue(it)
        }
    }

    @Composable
    override fun AcceptUI(viewModel: TitleSettingsViewModel) {
        ItemSettingsTitle(text = viewModel.title)
    }

}