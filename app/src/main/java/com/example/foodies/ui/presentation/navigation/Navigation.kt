package com.example.foodies.ui.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.foodies.ui.presentation.bucket.CartScreen
import com.example.foodies.ui.presentation.bucket.CartViewModel
import com.example.foodies.ui.presentation.catalog.CatalogScreen
import com.example.foodies.ui.presentation.catalog.CatalogViewModel
import com.example.foodies.ui.presentation.item_card.ItemCardScreen
import com.example.foodies.ui.presentation.item_card.ItemCardViewModel
import com.example.foodies.ui.presentation.search.SearchScreen
import com.example.foodies.ui.presentation.search.SearchViewModel
import com.example.foodies.ui.presentation.util.SplashScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {

  val navController = rememberNavController()
  val cartViewModel: CartViewModel = hiltViewModel()

  NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {

    composable(route = Screen.SplashScreen.route) {
      SplashScreen(
        onHomePage = { navController.navigate(Screen.CatalogScreen.route) }
      )
    }

    composable(route = Screen.CatalogScreen.route) {
      val catalogViewModel: CatalogViewModel = hiltViewModel()

      CatalogScreen(
        onSearchClicked = { navController.navigate(Screen.SearchScreen.route) },
        onBucketClicked = { navController.navigate(Screen.BucketScreen.route) },
        onCategoryClicked = { catalogViewModel.categoryFilter(it) },
        onFilterClicked = {},
        onItemClicked = { id ->
          navController.navigate(Screen.ItemScreen.route + "/$id")
        },
        onMinusClicked = { cartViewModel.deleteItem(it) },
        onPlusClicked = { cartViewModel.addItem(it) },
        catalogViewModel = catalogViewModel,
        cartViewModel = cartViewModel
      )
    }
    composable(route = Screen.BucketScreen.route) {
      CartScreen(
        onBackClicked = {
          cartViewModel.getCartContent()
          navController.popBackStack()
        },
        onMinusClicked = { cartViewModel.deleteItem(it) },
        onPlusClicked = { cartViewModel.addItem(it) },
        onOrderClicked = {},
        viewModel = cartViewModel
      )
    }
    composable(
      route = Screen.ItemScreen.route + "/{id}",
      arguments = listOf(navArgument("id") { type = NavType.IntType })
    ) {
      val itemViewModel: ItemCardViewModel = hiltViewModel()

      ItemCardScreen(
        onBackClicked = { navController.popBackStack() },
        addToCartClicked = { cartViewModel.addItem(it) },
        productState = itemViewModel.product,
      )
    }
    composable(
      route = Screen.SearchScreen.route
    ) {
      val searchViewModel: SearchViewModel = hiltViewModel()

      SearchScreen(
        onBackClicked = { navController.popBackStack() },
        viewModel = searchViewModel,
        cartViewModel = cartViewModel,
        onItemClicked = { id ->  navController.navigate(Screen.ItemScreen.route + "/$id") },
        onPlusClicked = { cartViewModel.addItem(it) },
        onMinusClicked = { cartViewModel.deleteItem(it) }
      )
    }
  }
}

@Preview
@Composable
fun NavigationPreview(modifier: Modifier = Modifier) {
  Navigation(modifier = modifier)
}