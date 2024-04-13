package com.example.foodies.ui.presentation.states

import com.example.domain.models.TagsModel

data class TagState(
  val tags: List<TagsModel>? = listOf(),
  val error: String = "",
  val isLoading: Boolean = false
)