package com.example.myapplication.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.R
import com.example.myapplication.data.repository.DataRepositoryHardCode
import com.example.myapplication.ui.destinations.DetailsUIDestination
import com.example.myapplication.ui.utils.SubscribeEvents
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@RootNavGraph(start = true)
@Destination
@Composable
fun ListUi(
    navigator: DestinationsNavigator
) {
    val viewModel = viewModel { listViewModel(DataRepositoryHardCode()) }
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    viewModel.SubscribeEvents {
        when (it) {
            is ListOutputs.OpenDetails ->
                navigator.navigate(DetailsUIDestination(it.item))
        }
    }

    ListUi(
        state = state,
        onOpenDetails = { viewModel.dispatch(ListMessage.OnOpenDetails(it)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListUi(
    state: ListState,
    onOpenDetails: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.list_title)) })
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                else -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        state.items.forEach {
                            Text(
                                text = it.text,
                                modifier = Modifier.clickable { onOpenDetails(it.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}