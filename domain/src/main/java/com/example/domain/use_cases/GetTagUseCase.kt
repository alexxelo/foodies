package com.example.domain.use_cases

import com.example.domain.Repository
import com.example.domain.Resource
import com.example.domain.models.TagsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTagUseCase @Inject constructor(private val repository: Repository) {
  operator fun invoke(): Flow<Resource<List<TagsModel>>> = flow {
    try {
      emit(Resource.Loading())
      val tags = repository.getTag()

      emit(Resource.Success(tags))

    } catch (e: Exception) {
      emit(Resource.Error("An unexpected error occurred: ${e.localizedMessage}"))
    }
  }
}