package com.example.foodies.ui.presentation.catalog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.Resource
import com.example.domain.models.CategoryModel
import com.example.domain.use_cases.GetCategoryUseCase
import com.example.domain.use_cases.GetProductsUseCase
import com.example.domain.use_cases.GetTagUseCase
import com.example.foodies.ui.presentation.states.CategoryState
import com.example.foodies.ui.presentation.states.ProductState
import com.example.foodies.ui.presentation.states.TagState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
  private val getTagUseCase: GetTagUseCase,
  private val getCategoryUseCase: GetCategoryUseCase,
  private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

  private val _tag = mutableStateOf(TagState())
  var tag: State<TagState> = _tag

  private val _category = mutableStateOf(CategoryState())
  var category: State<CategoryState> = _category

  private val _product = mutableStateOf(ProductState())
  var product: State<ProductState> = _product

//  private val _isOnCart = MutableLiveData<Boolean>()
//  var isOnCart: LiveData<Boolean> = _isOnCart

//  private val _cart = mutableStateOf(CartState())
//  var cart: State<CartState> = _cart

  private val _filteredList = mutableStateOf(ProductState())
  var filteredList: State<ProductState> = _filteredList

//  fun isOnCart(productsModel: ProductsModel): Boolean {
//    val item: ProductsModel? = cart.value.products?.find { it.id == productsModel.id }
//    return item != null
//  }


  init {
    getTag()
    getCategories()
    getProducts()
    //getCart()

    _category.value.categories?.firstOrNull()?.let {
      categoryFilter(it.id)
    }
  }
//  private fun getCart() {
//    _cart.value = CartState(
//      products = Cart.getCartContents(),
//      totalItems = Cart.getCartContents().size,
//      totalCost = Cart.getTotalPrice()
//    )
//  }

  private val _selectedCategory = mutableStateOf<CategoryModel?>(null)
  val selectedCategory: State<CategoryModel?> = _selectedCategory

  private val _isCategorySelected = mutableStateOf(false)
  val isCategorySelected: State<Boolean> = _isCategorySelected

  fun selectCategory(category: CategoryModel) {
    _selectedCategory.value = category
    _isCategorySelected.value = true
  }

  fun autoSelectCategory(categories: List<CategoryModel>) {
    if (!_isCategorySelected.value && categories.isNotEmpty()) {
      selectCategory(categories.first())
    }
  }


//  fun addToCart(productsModel: ProductsModel) {
//    Cart.addProduct(productsModel)
//    getCart()
//  }
//
//  fun deleteFromCartItem(productsModel: ProductsModel) {
//    Cart.removeProduct(productsModel)
//    getCart()
//  }

  fun categoryFilter(categoryId: Int) {
    _filteredList.value = ProductState(product.value.products?.filter { it.categoryId == categoryId })
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

  private fun getCategories() {
    getCategoryUseCase().onEach { result ->
      when (result) {
        is Resource.Loading -> {
          _category.value = CategoryState(isLoading = true)
        }

        is Resource.Error -> {
          _category.value = CategoryState(error = result.message ?: "An unexpected error occurred")
        }

        is Resource.Success -> {
          _category.value = CategoryState(categories = result.data)
          category.value.categories?.let { autoSelectCategory(it) }

        }
      }
    }.launchIn(viewModelScope)

  }

  private fun getProducts() {
    getProductsUseCase().onEach { result ->
      when (result) {
        is Resource.Loading -> {
          _product.value = ProductState(isLoading = true)
        }

        is Resource.Error -> {
          _product.value = ProductState(error = result.message ?: "An unexpected error occurred")
        }

        is Resource.Success -> {
          _product.value = ProductState(products = result.data)
          categoryFilter(selectedCategory.value!!.id)
        }
      }
    }.launchIn(viewModelScope)
  }

}