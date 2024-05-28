package com.example.foodies.ui.presentation.navigation


sealed class Screen(val route: String) {
  data object CatalogScreen : Screen("catalog_screen")
  data object BucketScreen : Screen("bucket_screen")
  data object ItemScreen : Screen("item_screen")
  data object SplashScreen : Screen("splash_screen")
  data object SearchScreen : Screen("search_screen")

}