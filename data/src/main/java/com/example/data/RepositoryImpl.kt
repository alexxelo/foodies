package com.example.data

import android.util.Log
import com.example.data.mapper.toModel
import com.example.domain.Repository
import com.example.domain.models.CategoryModel
import com.example.domain.models.ProductsModel
import com.example.domain.models.TagsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException


class RepositoryImpl(private val apiService: ApiService) : Repository {
  override suspend fun getCategory(): Flow<List<CategoryModel>> {
    return flow {
      val response = apiService.getCategories()
      if (response.isSuccessful) {
        val category = response.body() ?: emptyList()
        val categoryModel = category.map { it.toModel() }
        emit(categoryModel)
      } else {
        throw IOException("Error fetching categories")
      }
    }
  }

  override suspend fun getTag(): List<TagsModel> {
    return apiService.getTags().body()!!.map { it.toModel() }

  }

  override suspend fun getProducts(): List<ProductsModel> {
    return apiService.getProducts().body()!!.map { it.toModel() }

  }
}