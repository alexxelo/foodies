package com.example.domain.use_cases

import com.example.domain.Repository
import com.example.domain.Resource
import com.example.domain.models.CategoryModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCategoryUseCase @Inject constructor(private val repository: Repository) {
  operator fun invoke(): Flow<Resource<List<CategoryModel>>> = flow {
    try {
      emit(Resource.Loading())
      val category = repository.getCategory()
      emit(Resource.Success(category))
    } catch (e: Exception) {
      emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
    }
  }
}