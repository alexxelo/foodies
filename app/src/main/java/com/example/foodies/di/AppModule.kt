package com.example.foodies.di

import com.example.data.ApiService
import com.example.data.RepositoryImpl
import com.example.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
  @Provides
  @Singleton
  fun provideApi(): ApiService {
    val client = OkHttpClient.Builder()
      .addInterceptor { chain ->
        val request = chain.request().newBuilder()
          .build()
        chain.proceed(request)
      }
      .build()
    return Retrofit.Builder()
      .client(client)
      .baseUrl("https://anika1d.github.io/WorkTestServer/")
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(ApiService::class.java)
  }
}

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
  @Provides
  @Singleton
  fun provideRepository(
    api: ApiService,
  ): Repository {
    return RepositoryImpl(api)
  }
}
//
//@Module
//@InstallIn(SingletonComponent::class)
//object Cart {
//
//  private val productList = mutableListOf<ProductsModel>()
//
//  fun addProduct(product: ProductsModel) {
//    productList.add(product)
//  }
//
//  fun removeProduct(product: ProductsModel) {
//    productList.remove(product)
//  }
//
//  fun getTotalPrice(): Int {
//    return productList.sumOf { it.priceCurrent }
//  }
//
//  fun getCartContents(): List<ProductsModel> {
//    return productList
//  }
//
//  fun clearCart() {
//    productList.clear()
//  }
//}