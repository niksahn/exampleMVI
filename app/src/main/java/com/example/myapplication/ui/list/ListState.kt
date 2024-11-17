package com.example.myapplication.ui.list

import com.example.myapplication.data.entities.Item

data class ListState(
    val isLoading: Boolean = true,
    val items: List<Item> = emptyList()
)

sealed interface ListMessage {
    data class OnOpenDetails(val id: Int) : ListMessage
    data object Load : ListMessage
}

sealed interface ListOutputs {
    data class OpenDetails(val item: Item) : ListOutputs
}
