package com.example.data.mapper

import com.example.data.Category
import com.example.domain.models.CategoryModel


fun Category.toModel() = CategoryModel(
  id = id,
  name = name
)
fun CategoryModel.toEntity() = Category(
  id = id,
  name = name
)