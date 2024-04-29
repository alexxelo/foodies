package com.example.data.mapper

import com.example.data.Products
import com.example.domain.models.ProductsModel

fun Products.toModel() = ProductsModel(
  id = id,
  categoryId = categoryId,
  name = name,
  description = description,
  image = image,
  priceCurrent = priceCurrent,
  priceOld = priceOld,
  measure = measure,
  measureUnit = measureUnit,
  energyPer100Grams = energyPer100Grams,
  proteinsPer100Grams = proteinsPer100Grams,
  fatsPer100Grams = fatsPer100Grams,
  carbohydratesPer100Grams = carbohydratesPer100Grams,
  tagIds = tagIds
)

fun ProductsModel.toEntity() = Products(
  id = id,
  categoryId = categoryId,
  name = name,
  description = description,
  image = image,
  priceCurrent = priceCurrent,
  priceOld = priceOld,
  measure = measure,
  measureUnit = measureUnit,
  energyPer100Grams = energyPer100Grams,
  proteinsPer100Grams = proteinsPer100Grams,
  fatsPer100Grams = fatsPer100Grams,
  carbohydratesPer100Grams = carbohydratesPer100Grams,
  tagIds = tagIds
)