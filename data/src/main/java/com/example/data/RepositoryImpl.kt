package com.example.data

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
       if (response.isSuccessful){
         val category = response.body() ?: emptyList()
         val categoryModel = category.map { it.toModel() }
         emit(categoryModel)
       } else{
         throw IOException("Error fetching categories")
       }
     }
  }

  override suspend fun getTag(): Flow<List<TagsModel>> {
    return flow {
      val response = apiService.getTags()
      if (response.isSuccessful){
        val tags = response.body() ?: emptyList()
        val tagsModel = tags.map { it.toModel() }
        emit(tagsModel)
      } else{
        throw IOException("Error fetching categories")
      }
    }
  }

  override suspend fun getProducts(): Flow<List<ProductsModel>> {
    return flow {
      val response = apiService.getProducts()
      if (response.isSuccessful){
        val products = response.body() ?: emptyList()
        val productsModel = products.map { it.toModel() }
        emit(productsModel)
      } else{
        throw IOException("Error fetching categories")
      }
    }
  }
}