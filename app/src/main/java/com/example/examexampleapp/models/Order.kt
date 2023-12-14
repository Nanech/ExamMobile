package com.example.examexampleapp.models

data class Order(
    val id: Int,
    val address: String?,
    val dataTimeOrder: String?,
    val phone: String?,
    val comment: String?,
    val catalog: List<Catalog>?
)
