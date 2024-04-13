package com.example.domain.use_cases

import com.example.domain.Repository
import com.example.domain.Resource
import com.example.domain.models.ProductsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(private val repository: Repository) {
  operator fun invoke(): Flow<Resource<List<ProductsModel>>> = flow {
    try {
      emit(Resource.Loading())
      repository.getProducts().collect { productsModel ->
        emit(Resource.Success(productsModel))
      }
    } catch (e: Exception) {
      emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
    }
  }
}
