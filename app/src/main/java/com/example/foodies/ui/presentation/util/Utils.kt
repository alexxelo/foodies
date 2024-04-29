package com.example.foodies.ui.presentation.util

class PriceFormatter {
  companion object {
    fun formatPrice(price: Int?): Int {
      return price?.div(100) ?: 0
    }
  }
}
