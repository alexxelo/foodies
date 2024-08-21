package com.example.foodies.ui.presentation.catalog


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.models.CategoryModel
import com.example.domain.models.ProductsModel
import com.example.domain.models.TagsModel
import com.example.foodies.R
import com.example.foodies.ui.presentation.bucket.CartViewModel
import com.example.foodies.ui.presentation.util.ButtonAddToCart
import com.example.foodies.ui.presentation.util.ButtonsCounter
import com.example.foodies.ui.presentation.util.EmptyScreen
import com.example.foodies.ui.presentation.util.PriceFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
  modifier: Modifier = Modifier,
  onSearchClicked: () -> Unit,
  onBucketClicked: () -> Unit,
  onCategoryClicked: (categoryId: Int) -> Unit,
  onItemClicked: (id: Int) -> Unit,
  onFilterClicked: (filterOptions: FilterOptions) -> Unit,
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


  var isVegetarian by remember { mutableStateOf(false) }
  var isSpicy by remember { mutableStateOf(false) }
  var hasDiscount by remember { mutableStateOf(false) }

  val numberOfSelectedFilters = listOf(isVegetarian, isSpicy, hasDiscount).count { it }


  val sheetState = rememberModalBottomSheetState()
  var isSheetOpen by rememberSaveable { mutableStateOf(false) }

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
          Box(modifier = Modifier) {
            IconButton(onClick = {
              isSheetOpen = true
            }) {
              Icon(painter = painterResource(id = R.drawable.filter), contentDescription = "")
            }
            if (numberOfSelectedFilters > 0) {
              Box(
                modifier = Modifier
                  .size(21.dp)
                  .background(
                    color = colorResource(id = R.color.orange),
                    shape = CircleShape
                  )
                  .align(Alignment.TopEnd),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = numberOfSelectedFilters.toString(),
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }
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
          when {
            productsState.isLoading -> {
              CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
              )
            }

            productsState.products.isEmpty() -> {
              EmptyScreen(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(id = R.string.noFilterResult)
              )
            }

            else -> {
              LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier
                  .fillMaxSize()
              ) {
                items(
                  items = productsModelList,
                  key = { product ->
                    product.id.toString() + cartViewModel.cart.value.countRepeatedProducts(
                      product.id
                    )
                  }) { productsModel ->
                  ItemCard(
                    product = productsModel,
                    tag = tagState.tags,
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
            }
          }
        }

        if (isSheetOpen) {
          ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }) {
            Column(modifier = Modifier.padding(16.dp)) {

              Text(
                text = stringResource(id = R.string.choose),
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Medium,
                fontSize = 20.sp
              )
              CheckBox(
                stringResource(id = R.string.vegatarian),
                isChecked = isVegetarian,
                onCheckedChange = { isVegetarian = it })
              CheckBox(stringResource(id = R.string.spicy), isChecked = isSpicy, onCheckedChange = { isSpicy = it })
              CheckBox(stringResource(id = R.string.discount), isChecked = hasDiscount, onCheckedChange = { hasDiscount = it })
              TextButton(
                onClick = {
                  isSheetOpen = false
                  onFilterClicked(FilterOptions(isVegetarian, isSpicy, hasDiscount))
                },
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.textButtonColors(
                  containerColor = colorResource(id = R.color.orange),
                  contentColor = Color.White
                )
              ) {
                Text(text = stringResource(id = R.string.done), textAlign = TextAlign.Center)
              }
            }
          }
        }
        LaunchedEffect(cart.totalItems) {
          cartTotalItemsState.intValue = cart.totalItems
        }
      }

    }

  }
}

@Composable
fun CheckBox(
  text: String,
  isChecked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
      .padding(horizontal = 16.dp, vertical = 12.dp)
      .fillMaxWidth()
  ) {
    Text(text = text, modifier = Modifier.fillMaxWidth())
    Checkbox(
      checked = isChecked,
      onCheckedChange = onCheckedChange,
      colors = CheckboxDefaults.colors(
        checkedColor = colorResource(id = R.color.orange),
        uncheckedColor = Color.LightGray,
        checkmarkColor = Color.White
      )
    )
  }
  Divider(thickness = 1.dp, color = Color.LightGray)
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
        containerColor = Color.Transparent
      )
    }
  ) {
    Text(text = categoryModel.name, fontWeight = FontWeight.Medium)
  }
}


@Composable
fun ItemCard(
  modifier: Modifier = Modifier,
  product: ProductsModel,
  tag: List<TagsModel>? = null,
  isOnCart: Boolean,
  count: Int,
  addToCart: (product: ProductsModel) -> Unit,
  onItemClicked: (id: Int) -> Unit,
  onMinusClicked: () -> Unit,
  onPlusClicked: () -> Unit,
) {
  val cartItemsCount = remember { mutableIntStateOf(count) }

  LaunchedEffect(Unit) {
    cartItemsCount.intValue = count
  }
  Card(
    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.gray_bg)),
    modifier = modifier
      .padding(8.dp)
      .clickable { onItemClicked(product.id) }) {
    Box(modifier = Modifier) {
      product.tagIds.forEachIndexed { _, tagId ->
        val tags = tag?.find { it.id == tagId }
        val tagsColorsGradient = when (tagId) {
          3 -> {
            listOf(
              Color(0xFF729EF2),
              Color(0xFF9365C2),
              Color(0xFF452192)
            )
          }

          4 -> {
            listOf(
              Color(0xFFF97D23),
              Color(0xFFFB4E1E),
              Color(0xFFF83600)
            )
          }

          2 -> {
            listOf(
              Color(0xFFA8E063),
              Color(0xFF66BE3E),
              Color(0xFF56AB2F)
            )
          }

          else -> listOf(
            Color(0xFF729EF2),
            Color(0xFF9365C2),
            Color(0xFF452192)
          )
        }
        Box(
          modifier = Modifier
            .padding(10.dp)
            .size(24.dp)
            .background(
              brush = Brush.linearGradient(
                colors = tagsColorsGradient
              ), shape = CircleShape
            ), contentAlignment = Alignment.Center
        ) {
          tags?.let {
            val drawableResId = when (tagId) {
              2 -> R.drawable.lab2
              3 -> R.drawable.lab1
              4 -> R.drawable.lab4
              else -> R.drawable.lab1
            }
            Image(
              painter = painterResource(id = drawableResId), contentDescription = ""
            )
          }
        }
      }
      Image(painter = painterResource(id = R.drawable.photo), contentDescription = "")
    }
    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 12.dp)) {
      Text(text = product.name.replace(Regex("\\s+\\d+гр$"), ""), fontSize = 14.sp, maxLines = 1)
      Text(
        text = if (product.measure > 1) "${product.measure} ${product.measureUnit}" else " ",
        fontSize = 14.sp,
        color = colorResource(id = R.color.gray)
      )
      if (isOnCart) {
        ButtonsCounter(buttonColors = ButtonDefaults.buttonColors(containerColor = Color.White),
          count = cartItemsCount.intValue,
          onMinusClicked = { onMinusClicked() },
          onPlusClicked = { onPlusClicked() })
      } else {
        ButtonAddToCart(
          priceNew = PriceFormatter.formatPrice(product.priceCurrent),
          priceOld = PriceFormatter.formatPrice(product.priceOld),
          addToCart = { addToCart(product) }
        )
      }
    }
  }
}

@Preview(device = Devices.NEXUS_5)
@Composable
fun CatalogScreenPreview(modifier: Modifier = Modifier) {
  //CatalogScreen(modifier = modifier)
}
