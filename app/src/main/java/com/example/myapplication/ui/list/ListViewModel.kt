package com.example.myapplication.ui.list

import com.example.myapplication.data.repository.DataRepository
import com.example.myapplication.ui.utils.BaseViewModel

fun listViewModel(dataRepository: DataRepository) =
    BaseViewModel<ListState, ListOutputs, ListMessage>(
        initialState = ListState(),
        initialEvents = setOf(ListMessage.Load),
        processInputEvent = { event, state -> processInputEvent(dataRepository, event, state) }
    )

private suspend fun processInputEvent(
    dataRepository: DataRepository,
    event: ListMessage,
    state: ListState
): Pair<ListState, Set<ListOutputs>> =
    when (event) {
        ListMessage.Load ->
            state.copy(
                isLoading = false,
                items = dataRepository.getItems()
            ) to emptySet()

        is ListMessage.OnOpenDetails ->
            state to setOfNotNull(
                state.items
                    .firstOrNull { it.id == event.id }
                    ?.let { ListOutputs.OpenDetails(it) }
            )
    }