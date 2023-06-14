package com.aldajo92.mardanrobot._settings.model.visitor

import android.content.Context
import androidx.compose.runtime.Composable

class SettingsUIRender(private val context: Context, private val inputViewModel: InputViewModel<*>?) {

    @Composable
    fun AcceptUI(inputSettingsVisitor: InputSettingsVisitor) {
        inputViewModel?.VisitUI(context, inputSettingsVisitor)
    }

}
