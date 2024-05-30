package com.example.foodies.ui.presentation.search


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.models.ProductsModel
import com.example.foodies.R
import com.example.foodies.ui.presentation.bucket.CartViewModel
import com.example.foodies.ui.presentation.catalog.ItemCard
import com.example.foodies.ui.presentation.util.EmptyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
  modifier: Modifier = Modifier,
  onBackClicked: () -> Unit,
  onItemClicked: (id: Int) -> Unit,
  onMinusClicked: (product: ProductsModel) -> Unit,
  onPlusClicked: (product: ProductsModel) -> Unit,
  viewModel: SearchViewModel = hiltViewModel(),
  cartViewModel: CartViewModel = hiltViewModel()
) {
  val searchText by viewModel.searchText.collectAsState()
  //val searchResult by viewModel.searchResult.collectAsState()
  val isSearching by viewModel.isSearching.collectAsState()

  val products by viewModel.products.collectAsState()

  val tag = viewModel.tag.value

  Surface(
    modifier = Modifier.fillMaxWidth(), tonalElevation = 4.dp
  ) {

    Scaffold(topBar = {
      TopAppBar(title = {
        TextField(value = searchText,
          onValueChange = viewModel::onSearchTextChange,
          modifier = Modifier.fillMaxWidth(),
          placeholder = { Text(text = stringResource(R.string.find_product)) },
          singleLine = true,
          colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent, focusedContainerColor = Color.Transparent
          ),
          shape = TextFieldDefaults.shape,
          textStyle = LocalTextStyle.current.copy(color = Color.Black),
          trailingIcon = {
            IconButton(onClick = { viewModel.onSearchTextChange("") }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
          })
      }, navigationIcon = {
        IconButton(onClick = { onBackClicked() }) {
          Icon(
            imageVector = Icons.Filled.ArrowBack, tint = colorResource(id = R.color.orange), contentDescription = ""
          )
        }
      })
    }) { paddingValues ->
      Column(
        modifier = modifier
          .fillMaxSize()
          .padding(paddingValues)
      ) {
        if (searchText.isNotBlank()) {

          if (isSearching) {//true
            Box(modifier = Modifier.fillMaxSize()) {
              CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
              )
            }
          } else {
            if (products.isEmpty()) {
              EmptyScreen(
                modifier = Modifier.fillMaxSize(), text = stringResource(id = R.string.empty_search_result)
              )
            } else {
              LazyVerticalGrid(
                columns = GridCells.Fixed(2), modifier = Modifier
                  .fillMaxWidth()
                  .weight(1f)
              ) {
                items(
                  items = products,
                  key = { product -> product.id.toString() + cartViewModel.cart.value.countRepeatedProducts(product.id) }) { product ->
//
                  ItemCard(
                    product = product,
                    isOnCart = cartViewModel.isOnCart(productsModel = product),
                    tag = tag.tags,
                    count = cartViewModel.cart.value.countRepeatedProducts(product.id),
                    addToCart = { cartViewModel.addItem(it) },
                    onItemClicked = { onItemClicked(product.id) },
                    onMinusClicked = { onMinusClicked(product) },
                    onPlusClicked = { onPlusClicked(product) })
                }
              }
            }
          }
        } else {
          EmptyScreen(modifier = Modifier.fillMaxSize(), text = stringResource(id = R.string.empty_search_screen))
        }
      }
    }
  }
}


@Preview
@Composable
fun SearchScreenPreview(modifier: Modifier = Modifier) {
  //SearchScreen(modifier = modifier, {})
}