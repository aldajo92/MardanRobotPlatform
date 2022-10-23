package com.aldajo92.mardanrobot._settings

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aldajo92.mardanrobot._settings.model.visitor.*
import com.aldajo92.mardanrobot.ui.components.AppBarWithArrow

class SettingsActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                AppBarWithArrow(
                    title = "Settings",
                    onBackPressed = this::onBackPressed
                )
            }) {
                BodyContent()
            }
        }
    }

    @Composable
    fun BodyContent() {

        val listItems = listOf(
            CheckSettingsViewModel(title = "Enable BT", key = "defaultKey"),
            CheckSettingsViewModel(title = "Enable BT", key = "defaultKey"),
            TitleSettingsViewModel(title = "Enable BT"),
            CheckSettingsViewModel(title = "Enable BT", key = "defaultKey", defaultValue = true),
            ChoiceListSettingsViewModel(
                title = "Check list",
                key = "key",
                listSelection = listOf("item1", "item2")
            ),
            InputTextSettingsViewModel(title = "Check list", key = "key"),
        )

        val inputSettingsVisitorImpl = InputSettingsVisitorImpl()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(listItems) { item ->
                SettingsUIRender(item).AcceptUI(inputSettingsVisitorImpl)
            }
        }
    }

}
