package com.example.data.mapper

import com.example.data.Tags
import com.example.domain.models.TagsModel

fun Tags.toModel() = TagsModel(
  id = id, name = name
)

fun TagsModel.toEntity() = Tags(
  id = id, name = name
)