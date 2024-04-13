package com.example.data

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
  @GET("Categories.json")
  suspend fun getCategories(): Response<List<Category>>

  @GET("Tags.json")
  suspend fun getTags(): Response<List<Tags>>

  @GET("Products.json")
  suspend fun getProducts(): Response<List<Products>>

}