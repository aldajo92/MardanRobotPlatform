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
    fun AcceptUI(inputTextSettingsViewModel: InputTextSettingsViewModel)

    @Composable
    fun AcceptUI(choiceListSettingsViewModel: ChoiceListSettingsViewModel)

    @Composable
    fun AcceptUI(checkSettingsViewModel: CheckSettingsViewModel)

    @Composable
    fun AcceptUI(titleSettingsViewModel: TitleSettingsViewModel)

}

class InputSettingsVisitorImpl : InputSettingsVisitor {

    @Composable
    override fun AcceptUI(inputTextSettingsViewModel: InputTextSettingsViewModel) {
        ItemSettingsInputText()
    }

    @Composable
    override fun AcceptUI(choiceListSettingsViewModel: ChoiceListSettingsViewModel) {
        ItemSettingsChoiceList(
            text = "Choice value",
            values = choiceListSettingsViewModel.listSelection
        )
    }

    @Composable
    override fun AcceptUI(checkSettingsViewModel: CheckSettingsViewModel) {
        val checkState by checkSettingsViewModel.getSettingValueFlow().collectAsState(initial = false)
        ItemSettingsCheck(
            text = checkSettingsViewModel.title,
            checkState = checkState
        ) {
            checkSettingsViewModel.updateSettingValue(it)
        }
    }

    @Composable
    override fun AcceptUI(titleSettingsViewModel: TitleSettingsViewModel) {
        ItemSettingsTitle(text = titleSettingsViewModel.title)
    }

}