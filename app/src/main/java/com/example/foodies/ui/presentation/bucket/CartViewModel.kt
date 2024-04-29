package com.example.foodies.ui.presentation.bucket

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.Cart
import com.example.domain.models.ProductsModel
import com.example.foodies.ui.presentation.states.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

  private val _cart = mutableStateOf(CartState())
  var cart: State<CartState> = _cart
  private val _isOnCart = MutableLiveData<Boolean>()
  var isOnCart: LiveData<Boolean> = _isOnCart

  init {
    getCartContent()

  }

   fun getCartContent() {
    _cart.value = CartState(
      products = Cart.getCartContents(),
      totalCost = Cart.getTotalPrice(),
      totalItems = Cart.getCartContents().size
    )
  }

  fun isOnCart(productsModel: ProductsModel): Boolean {
    val item: ProductsModel? = cart.value.products?.find { it.id == productsModel.id }
    return item != null
  }
  fun deleteItem(productsModel: ProductsModel) {
    Cart.removeProduct(productsModel)
    getCartContent()
  }

  fun addItem(productsModel: ProductsModel) {
    Cart.addProduct(productsModel)
    getCartContent()

  }

  private fun clearCart() {
    Cart.clearCart()
  }
}