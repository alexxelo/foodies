package com.example.foodies.ui.presentation.states

import com.example.domain.models.CategoryModel

data class CategoryState (
  val categories: List<CategoryModel>? = listOf(),
  val error: String = "",
  val isLoading: Boolean = false
)