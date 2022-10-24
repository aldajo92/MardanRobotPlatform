package com.aldajo92.mardanrobot._settings.model.visitor

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aldajo92.mardanrobot._settings.ItemSettingsCheck
import com.aldajo92.mardanrobot._settings.ItemSettingsChoiceList
import com.aldajo92.mardanrobot._settings.ItemSettingsInputText
import com.aldajo92.mardanrobot._settings.ItemSettingsTitle

interface InputSettingsVisitor {

    @Composable
    fun AcceptUI(context: Context, viewModel: InputTextSettingsViewModel)

    @Composable
    fun AcceptUI(context: Context, viewModel: ChoiceListSettingsViewModel)

    @Composable
    fun AcceptUI(context: Context, viewModel: CheckSettingsViewModel)

    @Composable
    fun AcceptUI(context: Context, viewModel: TitleSettingsViewModel)

}

class InputSettingsVisitorImpl : InputSettingsVisitor {

    @Composable
    override fun AcceptUI(context: Context, viewModel: InputTextSettingsViewModel) {
        ItemSettingsInputText()
    }

    @Composable
    override fun AcceptUI(context: Context, viewModel: ChoiceListSettingsViewModel) {
        val selectedValue by viewModel.getSettingValueFlow().collectAsState(null)
        val listValues by viewModel.getListValueFlow().collectAsState(listOf())

        ItemSettingsChoiceList(
            context = context,
            text = viewModel.title,
            values = listValues,
            selectedValue = selectedValue,
            onNewItemAdded = {
                val newList = listValues
                    .toMutableList()
                    .apply {
                        this.add(it)
                    }
                    .toList()
                viewModel.updateList(newList)
            },
            onDeleteValueByPosition = { position ->
                val newList = listValues
                    .toMutableList()
                    .apply {
                        this.removeAt(position)
                    }
                    .toList()
                viewModel.updateList(newList)
            }
        ) {
            viewModel.updateSettingValue(it)
        }
    }

    @Composable
    override fun AcceptUI(context: Context, viewModel: CheckSettingsViewModel) {
        val checkState by viewModel.getSettingValueFlow().collectAsState(false)
        ItemSettingsCheck(
            text = viewModel.title,
            checkState = checkState
        ) {
            viewModel.updateSettingValue(it)
        }
    }

    @Composable
    override fun AcceptUI(context: Context, viewModel: TitleSettingsViewModel) {
        ItemSettingsTitle(text = viewModel.title)
    }

}