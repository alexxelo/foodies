package com.example.foodies.ui.presentation.item_card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.use_cases.GetProductByIdUseCase
import com.example.domain.use_cases.GetProductsUseCase
import com.example.foodies.ui.presentation.states.ProductInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemCardViewModel @Inject constructor(
  private val getProductByIdUseCase: GetProductByIdUseCase,
  private val getProductsUseCase: GetProductsUseCase,
  savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _product = MutableStateFlow(ProductInfoState())
  val product: StateFlow<ProductInfoState> = _product.asStateFlow()

  init {
    viewModelScope.launch(Dispatchers.Main) {
      savedStateHandle.get<Int>("id")?.let { id ->
        getProductById(id)
      }
    }
  }

  private fun getProductById(id: Int) {
    Log.d("main1", "getBy id = $id")
    getProductByIdUseCase(id).onEach { result ->
      Log.d("main", " result = $result")
      when (result) {
        is Resource.Success -> {
          _product.value = ProductInfoState(products = result.data)
        }

        is Resource.Loading -> {
          _product.value = ProductInfoState(isLoading = true)
        }

        is Resource.Error -> {
          _product.value = ProductInfoState(error = result.message ?: "An unexpected error occurred")
        }
      }
    }.launchIn(viewModelScope)
  }

}