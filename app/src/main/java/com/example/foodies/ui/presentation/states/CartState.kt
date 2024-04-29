package com.example.foodies.ui.presentation.states

import com.example.domain.models.ProductsModel

data class CartState(
  val products: List<ProductsModel>? = emptyList(),
  val totalCost: Int = 0,
  val totalItems: Int = 0
){
  fun countRepeatedProducts(productId: Int): Int {
    return products?.filter { it.id == productId }?.size ?: 1
  }
}
