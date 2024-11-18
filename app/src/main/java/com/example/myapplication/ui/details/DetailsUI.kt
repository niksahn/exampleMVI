package com.example.myapplication.ui.details

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.data.entities.Item
import com.example.myapplication.ui.utils.SubscribeEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Destination
@Composable
fun DetailsUI(
    item: Item,
    navigator: DestinationsNavigator,
) {
    val viewModel = viewModel { detailsViewModel(DetailsState(item)) }
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    viewModel.SubscribeEvents {
        when (it) {
            DetailsOutputs.Close -> navigator.navigateUp()
        }
    }

    DetailsUiContent(state, onClose = { viewModel.dispatch(DetailsMessage.Close) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsUiContent(
    state: DetailsState,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.details_title)) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onClose)
                    )
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.padding(it), contentAlignment = Alignment.Center
        ) { Text(state.item.text) }
    }
}