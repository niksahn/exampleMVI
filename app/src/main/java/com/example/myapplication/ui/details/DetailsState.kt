package com.example.myapplication.ui.details

import com.example.myapplication.data.entities.Item

data class DetailsState(val item: Item)

sealed interface DetailsMessage {
    data object Close : DetailsMessage
}

sealed interface DetailsOutputs {
    data object Close : DetailsOutputs
}
