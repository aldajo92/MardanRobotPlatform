package com.aldajo92.mardanrobot._settings.model.visitor

import androidx.compose.runtime.Composable

class SettingsUIRender(private val inputViewModel: InputViewModel<*>?) {

    @Composable
    fun AcceptUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputViewModel?.VisitUI(inputSettingsVisitor)
    }

}