package com.example.data


data class Category(
  val id: Int,
  val name: String,
)

data class Tags(
  val id: Int,
  val name: String,
)

data class Products(
  val id: Int,
  val categoryId: Int,
  val name: String,
  val description: String,
  val priceCurrent: Int,
  val priceOld: Int?,
  val measure: Int,
  val measureUnit: Int,
  val energyPer100Grams: Double,
  val proteinsPer100Grams: Double,
  val fatsPer100Grams: Double,
  val carbohydratesPer100Grams: Double,
  val tagIds: List<Tags>,
)
