package com.example.foodies.ui.presentation.states

import com.example.domain.models.ProductsModel

data class ProductInfoState(
  val products: ProductsModel? = null,
  val error: String = "",
  val isLoading: Boolean = false
)
