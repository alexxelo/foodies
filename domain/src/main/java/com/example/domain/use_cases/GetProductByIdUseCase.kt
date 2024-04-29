package com.example.domain.use_cases

import com.example.domain.Repository
import com.example.domain.Resource
import com.example.domain.models.ProductsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
  private val repository: Repository
) {
  operator fun invoke(itemId: Int): Flow<Resource<ProductsModel>> = flow {
    try {
      emit(Resource.Loading())
      val prod = repository.getProducts().find { product ->
        product.id == itemId
      }

      if (prod != null)
        emit(Resource.Success(prod))

    } catch (e: Exception) {
      emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
    }
  }
}