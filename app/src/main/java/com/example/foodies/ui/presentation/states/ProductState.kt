package com.example.foodies.ui.presentation.states

import com.example.domain.models.ProductsModel

data class ProductState (
  val products: List<ProductsModel>? = listOf(),
  val error: String = "",
  val isLoading: Boolean = false
)