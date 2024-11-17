package com.example.myapplication.data.repository

import com.example.myapplication.data.entities.Item

interface DataRepository {
    suspend fun getItems(): List<Item>
}