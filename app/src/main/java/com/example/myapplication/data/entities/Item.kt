package com.example.myapplication.data.entities
import kotlinx.serialization.Serializable

@Serializable
data class Item(val id: Int, val text: String)