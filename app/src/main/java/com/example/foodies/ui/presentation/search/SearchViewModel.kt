package com.example.foodies.ui.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.ProductsModel
import com.example.domain.use_cases.GetProductsUseCase
import com.example.domain.use_cases.GetTagUseCase
import com.example.foodies.ui.presentation.states.ProductState
import com.example.foodies.ui.presentation.states.TagState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchResult(
  //val isSearching: Boolean,
  val products: List<ProductsModel>,
  val error: String? = null
)

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val getProductsUseCase: GetProductsUseCase,
  private val getTagUseCase: GetTagUseCase,
) : ViewModel() {

  private val _tag = mutableStateOf(TagState())
  var tag: State<TagState> = _tag

  private val _searchText = MutableStateFlow("")
  val searchText = _searchText.asStateFlow()

  private val _isSearching = MutableStateFlow(false)
  val isSearching = _isSearching.asStateFlow()
//
//  private val _searchResult = MutableStateFlow(SearchResult(products = emptyList()))
//  val searchResult: StateFlow<SearchResult> = _searchResult

  private val _allProducts = MutableStateFlow(ProductState())

  private val _products = MutableStateFlow(emptyList<ProductsModel>())

  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
  val products = searchText
    .debounce(3000L)
    .onEach { _isSearching.value = true }
    .flatMapLatest { text ->
      _allProducts.map { productState ->
        productState.products?.filter {
          it.doesMatchSearchQuery(text)
        } ?: emptyList()
      }
    }
    .onEach {
      _isSearching.value = false
    }
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      emptyList()
    )

//  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//  val products = searchText
//    .debounce(1000L)
//    .onEach { _isSearching.value = true }
//    .flatMapLatest { text ->
//      _allProducts.map { productState ->
//        productState.products?.filter {
//          it.doesMatchSearchQuery(text)
//        } ?: emptyList()

//        if (text.isBlank()) {
//          productState.products ?: emptyList()
//        } else {
//          val filteredProducts = productState.products?.filter {
//            it.doesMatchSearchQuery(text)
//          }
//          filteredProducts ?: emptyList()
//        }
//      }
//    }
//    .onEach { result ->
//      _isSearching.value = false
//    }
//    .stateIn(
//      viewModelScope,
//      SharingStarted.WhileSubscribed(5000),
//      emptyList()
//    )
//  val products = searchText
//    .debounce(3000L)
//    .filter { it.isNotBlank() }
//    .onEach {
//      _isSearching.value = true
//    }
//    .flatMapLatest { text ->
//      _allProducts.map { productState ->
//        val filteredProducts = productState.products?.filter {
//          it.doesMatchSearchQuery(text)
//        } ?: emptyList()
//
//        val searchResult = SearchResult(products = filteredProducts)
//        if (filteredProducts.isEmpty()) {
//          searchResult.copy(error = "No results found")
//        } else {
//          searchResult
//        }
//      }
//    }
//    .onEach {
//      _isSearching.value = false
//    }
//    .stateIn(
//      viewModelScope,
//      SharingStarted.WhileSubscribed(5000),
//      SearchResult(products = emptyList())
//    )


  init {
    getProducts()
    getTag()

    viewModelScope.launch {
      _allProducts.collect { productsState ->
        _products.value = productsState.products ?: emptyList()
      }
    }
  }

  fun onSearchTextChange(text: String) {
    _searchText.value = text
  }

  private fun getTag() {
    getTagUseCase().onEach { result ->
      when (result) {
        is Resource.Loading -> {
          _tag.value = TagState(isLoading = true)
        }

        is Resource.Error -> {
          _tag.value = TagState(error = result.message ?: "An unexpected error occurred")
        }

        is Resource.Success -> {
          _tag.value = TagState(tags = result.data)
        }
      }
    }.launchIn(viewModelScope)
  }


  private fun getProducts() {
    getProductsUseCase().onEach { result ->
      when (result) {
        is Resource.Loading -> {
          _allProducts.value = ProductState(isLoading = true)
        }

        is Resource.Error -> {
          _allProducts.value = ProductState(error = result.message ?: "An unexpected error occurred")
        }

        is Resource.Success -> {
          _allProducts.value = ProductState(products = result.data)
        }
      }
    }.launchIn(viewModelScope)
  }
}