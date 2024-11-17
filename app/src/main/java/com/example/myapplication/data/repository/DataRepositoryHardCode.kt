package com.example.myapplication.data.repository

import com.example.myapplication.data.entities.Item
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class DataRepositoryHardCode : DataRepository {
    override suspend fun getItems(): List<Item> {
        runBlocking { delay(2000) }
        return listOf(
            Item(1, "1_АХАХАХАХХАХАХАХХААХАХ"),
            Item(2, "2_АХАХАХАХХАХАХАХХААХАХ"),
            Item(3, "3_АХАХАХАХХАХАХАХХААХАХ"),
            Item(4, "4_АХАХАХАХХАХАХАХХААХАХ")
        )
    }
}