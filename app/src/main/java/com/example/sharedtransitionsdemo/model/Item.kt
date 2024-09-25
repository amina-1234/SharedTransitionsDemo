package com.example.sharedtransitionsdemo.model

import com.example.sharedtransitionsdemo.R
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int,
    val image: Int,
    val title: String = "Item $id",
)

val items = List(20) {
    Item(
        id = it + 1,
        image = R.drawable.img
    )
}