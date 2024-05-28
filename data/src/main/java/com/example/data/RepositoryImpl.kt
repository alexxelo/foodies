package com.example.data

import com.example.data.mapper.toModel
import com.example.domain.Repository
import com.example.domain.models.CategoryModel
import com.example.domain.models.ProductsModel
import com.example.domain.models.TagsModel
import java.io.IOException


class RepositoryImpl(private val apiService: ApiService) : Repository {
  override suspend fun getCategory(): List<CategoryModel> {
    val response = apiService.getCategories()
    if (response.isSuccessful) {
      val category = response.body() ?: emptyList()
      return category.map { it.toModel() }
    } else {
      throw IOException("Error fetching categories")
    }
//    return flow {
//      val response = apiService.getCategories()
//      if (response.isSuccessful) {
//        val category = response.body() ?: emptyList()
//        val categoryModel = category.map { it.toModel() }
//        emit(categoryModel)
//      } else {
//        throw IOException("Error fetching categories")
//      }
//    }
  }

  override suspend fun getTag(): List<TagsModel> {
    //return apiService.getTags().body()?.map { it.toModel() }
    val response = apiService.getTags()
    if (response.isSuccessful) {
      val tags = response.body() ?: emptyList()
      return tags.map { it.toModel() }
    } else {
      throw IOException("Error fetching categories")
    }

  }

  override suspend fun getProducts(): List<ProductsModel> {
    //return apiService.getProducts().body()!!.map { it.toModel() }
    val response = apiService.getProducts()
    if (response.isSuccessful) {
      val products = response.body() ?: emptyList()
      return products.map { it.toModel() }
    } else {
      throw IOException("Error fetching categories")
    }
  }
}