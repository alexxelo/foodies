package com.example.foodies.ui.presentation.bucket


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.models.ProductsModel
import com.example.foodies.R
import com.example.foodies.ui.presentation.util.ButtonsCounter
import com.example.foodies.ui.presentation.util.EmptyScreen
import com.example.foodies.ui.presentation.util.PriceFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
  modifier: Modifier = Modifier,
  onBackClicked: () -> Unit,
  onMinusClicked: (product: ProductsModel) -> Unit,
  onPlusClicked: (product: ProductsModel) -> Unit,
  onOrderClicked: () -> Unit,
  viewModel: CartViewModel = hiltViewModel()
) {

  val cart = viewModel.cart.value
  val cartTotalItemsState = remember { mutableIntStateOf(cart.totalItems) }

  LaunchedEffect(cart.totalItems) {
    cartTotalItemsState.intValue = cart.totalItems
  }
  Scaffold(topBar = {
    TopAppBar(
      modifier = Modifier,
      title = { Text(text = stringResource(R.string.bucket), fontWeight = FontWeight.SemiBold) },
      navigationIcon = {
        IconButton(onClick = { onBackClicked() }) {
          Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = colorResource(id = R.color.orange),
            contentDescription = ""
          )
        }
      }
    )
  },
    bottomBar = {
      if (cartTotalItemsState.intValue > 0) {
        BottomAppBar(containerColor = Color.White) {
          Button(
            onClick = { onOrderClicked() },
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              contentColor = Color.White,
              containerColor = colorResource(id = R.color.orange)
            ),
            contentPadding = PaddingValues(16.dp)
          ) {
            Text(
              text = stringResource(R.string.order_for, PriceFormatter.formatPrice(cart.totalCost)) + " ₽",
              lineHeight = 16.sp,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }) { padding ->
    Surface(
      modifier = modifier
        .fillMaxSize()
        .padding(padding),
      color = Color.White,
      tonalElevation = 50.dp
    ) {
      cart.products?.let { products ->
        val uniqueProducts = products.distinctBy { it.id }
        LazyColumn(modifier = modifier) {
          items(
            items = uniqueProducts,
            key = { product -> product.id + viewModel.cart.value.countRepeatedProducts(product.id) }) { product ->

            Item(
              productName = product.name,
              priceCurrent = PriceFormatter.formatPrice(product.priceCurrent),
              priceOld = PriceFormatter.formatPrice(product.priceOld),
              count = viewModel.cart.value.countRepeatedProducts(product.id),
              onMinusClicked = { onMinusClicked(product) },
              onPlusClicked = { onPlusClicked(product) },
            )
            Divider(thickness = 1.dp, color = Color.LightGray)

          }
        }
      }
      if (cart.totalItems <= 0)
        EmptyScreen(Modifier.fillMaxSize(), stringResource(R.string.empty_cart_text))
    }

  }
}


@Composable
fun Item(
  productName: String,
  priceCurrent: Int,
  priceOld: Int,
  count: Int = 1,
  onMinusClicked: () -> Unit,
  onPlusClicked: () -> Unit,
) {
  Row(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalAlignment = Alignment.Top
  ) {
    Image(
      painter = painterResource(id = R.drawable.photo),
      modifier = Modifier.size(96.dp), contentDescription = ""
    )
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(),
        text = productName, fontSize = 14.sp, textAlign = TextAlign.Left, lineHeight = 20.sp
      )
      Spacer(modifier = Modifier.height(12.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
      ) {
        ButtonsCounter(
          modifier = Modifier.weight(2f),
          buttonColors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.light_gray)),
          count = count,
          onMinusClicked = { onMinusClicked() },
          onPlusClicked = { onPlusClicked() }
        )
        Column(
          modifier = Modifier
            .weight(1f),
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          Text(text = "$priceCurrent ₽ ", fontSize = 16.sp, fontWeight = FontWeight.Medium)
          if (priceOld > 0) {
            Text(
              text = "$priceOld ₽ ",
              fontSize = 14.sp,
              color = Color.Gray,
              textDecoration = TextDecoration.LineThrough
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun CartScreenPreview(modifier: Modifier = Modifier) {
  //EmptyScreen()
}