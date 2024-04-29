package com.example.domain

import com.example.domain.models.CategoryModel
import com.example.domain.models.ProductsModel
import com.example.domain.models.TagsModel
import kotlinx.coroutines.flow.Flow

interface Repository {

  suspend fun getCategory(): Flow<List<CategoryModel>>
  suspend fun getTag(): List<TagsModel>
  suspend fun getProducts(): List<ProductsModel>

}