package com.example.domain.use_cases

import com.example.domain.Repository
import com.example.domain.Resource
import com.example.domain.models.ProductsModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: Repository) {
  @OptIn(ExperimentalCoroutinesApi::class)
  operator fun invoke(): Flow<Resource<List<ProductsModel>>> = channelFlow{
    try {
      send(Resource.Loading())
      val prod = repository.getProducts()
      send(Resource.Success(prod))

    } catch (e: Exception) {
      send(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
    }
  }
}
