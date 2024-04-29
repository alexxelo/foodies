package com.example.foodies.ui.presentation.catalog


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.models.CategoryModel
import com.example.domain.models.ProductsModel
import com.example.foodies.R
import com.example.foodies.ui.presentation.bucket.CartViewModel
import com.example.foodies.ui.presentation.util.PriceFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
  modifier: Modifier = Modifier,
  onSearchClicked: () -> Unit,
  onFilterClicked: () -> Unit,
  onBucketClicked: () -> Unit,
  onCategoryClicked: (categoryId: Int) -> Unit,
  onItemClicked: (id: Int) -> Unit,
  onMinusClicked: (product: ProductsModel) -> Unit,
  onPlusClicked: (product: ProductsModel) -> Unit,
  catalogViewModel: CatalogViewModel = hiltViewModel(),
  cartViewModel: CartViewModel = hiltViewModel()
) {

  val categoryState = catalogViewModel.category.value
  val productsState = catalogViewModel.filteredList.value

  val tagState = catalogViewModel.tag.value

  val cart = cartViewModel.cart.value

  val selectedCategory = catalogViewModel.selectedCategory.value


  val cartTotalItemsState = remember { mutableIntStateOf(cart.totalItems) }

  LaunchedEffect(Unit) {
    catalogViewModel.autoSelectCategory(categoryState.categories ?: emptyList())
  }

  Scaffold(
    topBar = {
      CenterAlignedTopAppBar(
        title = {
          Icon(
            painter = painterResource(id = R.drawable.logo__1_), tint = colorResource(id = R.color.orange),
            contentDescription = ""
          )
        },
        actions = {
          IconButton(onClick = { onSearchClicked() }) {
            Icon(
              painter = painterResource(id = R.drawable.search),
              contentDescription = ""
            )
          }
        },
        navigationIcon = {
          IconButton(onClick = { onFilterClicked() }) {
            Icon(painter = painterResource(id = R.drawable.filter), contentDescription = "")
          }
        }
      )
    },
    bottomBar = {
      if (cartTotalItemsState.intValue != 0) {
        BottomAppBar(containerColor = Color.White) {
          Button(
            onClick = { onBucketClicked() },
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              contentColor = Color.White,
              containerColor = colorResource(id = R.color.orange)
            )
          ) {
            Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "")
            Text(text = "${PriceFormatter.formatPrice(cart.totalCost)}  ₽ ", modifier.padding(horizontal = 8.dp))
          }
        }
      }
    }
  ) { padding ->
    Column(modifier.padding(paddingValues = padding)) {
      categoryState.categories?.let {
        LazyRow(
          modifier = Modifier
            .fillMaxWidth(),
        ) {
          items(items = it, key = { it.id }) { categoryModel ->
            val isSelected = selectedCategory == categoryModel
            CategoryItem(
              categoryModel = categoryModel,
              isActive = isSelected,
              onCategoryClicked = {
                onCategoryClicked(categoryModel.id)
                catalogViewModel.selectCategory(categoryModel)
              }
            )
          }
        }
      }
      productsState.products?.let { productsModelList ->
        Box(modifier = Modifier.fillMaxSize()) {
          LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
              .fillMaxSize()
          ) {
            items(
              items = productsModelList,
              key = { productsModelList -> productsModelList.id.toString() + cartViewModel.cart.value.countRepeatedProducts(productsModelList.id) }) { productsModel ->
              ItemCard(
                products = productsModel,
                onItemClicked = { id -> onItemClicked(id) },
                isOnCart = cartViewModel.isOnCart(productsModel = productsModel),
                count = cartViewModel.cart.value.countRepeatedProducts(productsModel.id),
                addToCart = {
                  cartViewModel.addItem(it)
                },
                onMinusClicked = { onMinusClicked(productsModel) },
                onPlusClicked = { onPlusClicked(productsModel) }
              )
            }

          }
          if (productsState.isLoading) {
            CircularProgressIndicator(
              modifier = Modifier
                .align(Alignment.Center)
            )
          }
        }
      }
    }
  }
  LaunchedEffect(cart.totalItems) {
    cartTotalItemsState.intValue = cart.totalItems
  }
}


@Composable
fun CategoryItem(
  categoryModel: CategoryModel, isActive: Boolean = true,
  onCategoryClicked: () -> Unit,
) {
  Button(
    onClick = { onCategoryClicked() },
    modifier = Modifier.padding(horizontal = 8.dp),
    shape = RoundedCornerShape(10.dp),
    colors = if (isActive) {
      ButtonDefaults.buttonColors(
        contentColor = Color.White,
        containerColor = colorResource(id = R.color.orange)
      )
    } else {
      ButtonDefaults.buttonColors(
        contentColor = Color.Black,
        containerColor = Color.White
      )
    }
  ) {
    Text(text = categoryModel.name, fontWeight = FontWeight.Medium)
  }
}


@Composable
fun ItemCard(
  modifier: Modifier = Modifier,
  products: ProductsModel,
  tag: Boolean = true,
  isOnCart: Boolean,
  count: Int,
  addToCart: (product: ProductsModel) -> Unit,
  onItemClicked: (id: Int) -> Unit,
  onMinusClicked: () -> Unit,
  onPlusClicked: () -> Unit,
) {
  val cartItemsCount = remember { mutableStateOf(count) } // Используем переданное значение как начальное значение

  LaunchedEffect(Unit) {
    cartItemsCount.value = count // Обновляем значение при изменении count
  }
  Card(
    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.gray_bg)),
    modifier = modifier
      .padding(8.dp)
      .clickable { onItemClicked(products.id) }) {

    Image(painter = painterResource(id = R.drawable.photo), contentDescription = "")
    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
      Text(text = products.name, fontSize = 14.sp, maxLines = 1)
      Text(text = "${products.measure} ${products.measureUnit}", fontSize = 14.sp, color = colorResource(id = R.color.gray))
      if (isOnCart) {
        ButtonsCounter(buttonColors = ButtonDefaults.buttonColors(containerColor = Color.White),
          count = cartItemsCount.value,
          onMinusClicked = { onMinusClicked() },
          onPlusClicked = { onPlusClicked() })
      } else {
        ButtonAddToCart(
          priceNew = PriceFormatter.formatPrice(products.priceCurrent),
          priceOld = PriceFormatter.formatPrice(products.priceOld),
          addToCart = { addToCart(products) }
        )
      }
    }
  }
}

@Composable
fun ButtonAddToCart(priceNew: Int, priceOld: Int, addToCart: () -> Unit) {
  TextButton(modifier = Modifier.fillMaxWidth(),
    colors = ButtonDefaults.textButtonColors(containerColor = Color.White),
    shape = RoundedCornerShape(10.dp),
    onClick = { addToCart() }) {
    Text(text = "$priceNew ₽ ", fontWeight = FontWeight.Medium, color = colorResource(id = R.color.dark_gray))
    if (priceOld > 0) {
      Text(text = "$priceOld ₽", textDecoration = TextDecoration.LineThrough, color = Color.LightGray, fontSize = 14.sp)
    }
  }
}

@Composable
fun ButtonsCounter(
  modifier: Modifier = Modifier,
  buttonColors: ButtonColors,
  count: Int,
  onMinusClicked: () -> Unit,
  onPlusClicked: () -> Unit,
) {

  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Button(
      modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
      onClick = { onMinusClicked() },
      colors = buttonColors,
      shape = RoundedCornerShape(10.dp),
      contentPadding = PaddingValues(10.dp),
    ) {
      Icon(
        painter = painterResource(id = R.drawable.minus),
        contentDescription = "",
        tint = colorResource(id = R.color.orange)
      )
    }
    Text(text = "$count", fontWeight = FontWeight.Medium, fontSize = 20.sp)

    Button(
      modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
      onClick = { onPlusClicked() },
      colors = buttonColors,
      shape = RoundedCornerShape(10.dp),
      contentPadding = PaddingValues(10.dp),
    ) {
      Icon(
        painter = painterResource(id = R.drawable.plus),
        contentDescription = "",
        tint = colorResource(id = R.color.orange)
      )
    }
  }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun CatalogScreenPreview(modifier: Modifier = Modifier) {
  //CatalogScreen(modifier = modifier)

}
