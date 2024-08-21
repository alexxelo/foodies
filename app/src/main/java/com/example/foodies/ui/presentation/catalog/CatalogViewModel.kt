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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FilterOptions(
  val isVegetarian: Boolean = false,
  val isSpicy: Boolean = false,
  val hasDiscount: Boolean = false
)

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


  private val _filteredList = mutableStateOf(ProductState(isLoading = true))
  var filteredList: State<ProductState> = _filteredList

  private val _selectedCategory = mutableStateOf<CategoryModel?>(null)
  val selectedCategory: State<CategoryModel?> = _selectedCategory

  private val _isCategorySelected = mutableStateOf(false)

  private val _allProducts = mutableStateOf(ProductState())


  init {
    getTag()
    getCategories()
    getProducts()

    _category.value.categories?.firstOrNull()?.let {
      categoryFilter(it.id)
    }
  }

  fun selectCategory(category: CategoryModel) {
    _selectedCategory.value = category
    _isCategorySelected.value = true
  }

  private fun autoSelectCategory(categories: List<CategoryModel>) {
    if (!_isCategorySelected.value && categories.isNotEmpty()) {
      selectCategory(categories.first())
    }
  }

  fun categoryFilter(categoryId: Int) {
    _filteredList.value = ProductState(_allProducts.value.products?.filter { it.categoryId == categoryId })
  }

  fun applyFilters(filterOptions: FilterOptions) {
    val tagsToFilter = mutableListOf<Int>()
    if (filterOptions.isVegetarian) tagsToFilter.add(2)
    if (filterOptions.isSpicy) tagsToFilter.add(4)
    if (filterOptions.hasDiscount) tagsToFilter.add(3)
    viewModelScope.launch {
      _filteredList.value = ProductState(isLoading = true)
      delay(500)
      val filteredProducts = _allProducts.value.products?.filter { product ->
        tagsToFilter.all { it in product.tagIds }
      } ?: emptyList()

      _filteredList.value = ProductState(
        filteredProducts
      )
    }
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
          _allProducts.value = ProductState(isLoading = true)
        }

        is Resource.Error -> {
          _allProducts.value = ProductState(error = result.message ?: "An unexpected error occurred", isLoading = false)
        }

        is Resource.Success -> {
          _allProducts.value = ProductState(products = result.data, isLoading = false)
          categoryFilter(selectedCategory.value!!.id)
        }
      }
    }.launchIn(viewModelScope)
  }

}