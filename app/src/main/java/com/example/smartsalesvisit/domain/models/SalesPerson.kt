package com.example.smartsalesvisit.domain.models

data class SalesPerson(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String? = null,
    val territory: String? = null,
    val createdAt: Long = 0L
)
