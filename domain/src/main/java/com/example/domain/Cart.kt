package com.example.domain

import com.example.domain.models.ProductsModel

object Cart {

  private val productList = mutableListOf<ProductsModel>()

  fun addProduct(product: ProductsModel) {
    productList.add(product)
  }

  fun removeProduct(product: ProductsModel) {
    productList.remove(product)
  }

  fun getTotalPrice(): Int {
    return productList.sumOf { it.priceCurrent }
  }

  fun getCartContents(): List<ProductsModel> {
    return productList
  }

  fun clearCart() {
    productList.clear()
  }
}