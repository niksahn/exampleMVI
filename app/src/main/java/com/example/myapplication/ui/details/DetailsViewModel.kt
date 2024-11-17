package com.example.myapplication.ui.details

import com.example.myapplication.ui.utils.BaseViewModel

fun detailsViewModel(initial: DetailsState) =
    BaseViewModel(
        initialState = initial,
        processInputEvent = ::processInputEvent
    )

fun processInputEvent(
    event: DetailsMessage,
    state: DetailsState
): Pair<DetailsState, Set<DetailsOutputs>> =
    when (event) {
        DetailsMessage.Close -> state to setOf(DetailsOutputs.Close)
    }
