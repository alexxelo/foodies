package com.example.foodies.ui.presentation.item_card


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.models.ProductsModel
import com.example.foodies.R
import com.example.foodies.ui.presentation.states.ProductInfoState
import com.example.foodies.ui.presentation.util.PriceFormatter
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ItemCardScreen(
  modifier: Modifier = Modifier,
  onBackClicked: () -> Unit,
  addToCartClicked: (product:ProductsModel) -> Unit,
  productState: StateFlow<ProductInfoState>,
) {
  val itemState = productState.collectAsStateWithLifecycle()

  itemState.value.products?.let { item ->

    Scaffold(
      bottomBar = {
        BottomAppBar(containerColor = Color.White) {
          Button(
            onClick = {
              addToCartClicked(item)
            },
            modifier = modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 12.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
              contentColor = Color.White,
              containerColor = colorResource(id = R.color.orange)
            )
          ) {
            Text(
              text = stringResource(
                R.string.in_cart_for,
                PriceFormatter.formatPrice(item.priceCurrent)
              ) + " ₽"
            )
          }
        }
      }
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(it)
      )
      {
        item {
          Box(modifier = Modifier.fillMaxSize()) {
            Image(
              painter = painterResource(id = R.drawable.photo), contentDescription = "",
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxWidth()
            )
            Button(
              onClick = { onBackClicked() },
              modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
              shape = CircleShape,
              contentPadding = PaddingValues(10.dp),
              colors = ButtonDefaults.buttonColors(containerColor = Color.White),
              elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
            ) {
              Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                tint = colorResource(id = R.color.black)
              )
            }
          }
          FoodTitle(item.name, item.description)
          Divider(thickness = 1.dp, color = Color.LightGray)
          NutritionalValue(title = stringResource(R.string.measure), value = "${item.measure} ${item.measureUnit}")
          Divider(thickness = 1.dp, color = Color.LightGray)
          NutritionalValue(title = stringResource(R.string.energy), value = "${item.energyPer100Grams} ккал")
          Divider(thickness = 1.dp, color = Color.LightGray)
          NutritionalValue(
            title = stringResource(R.string.proteins),
            value = "${item.proteinsPer100Grams} ${item.measureUnit}"
          )
          Divider(thickness = 1.dp, color = Color.LightGray)
          NutritionalValue(title = stringResource(R.string.fats), value = "${item.fatsPer100Grams} ${item.measureUnit}")
          Divider(thickness = 1.dp, color = Color.LightGray)
          NutritionalValue(
            title = stringResource(R.string.carbohydrates),
            value = "${item.carbohydratesPer100Grams} ${item.measureUnit}"
          )
        }
      }
    }
  }
}


@Composable
fun FoodTitle(productName: String, productDesc: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 24.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Text(text = productName, color = Color.Black, fontSize = 30.sp)
    Text(text = productDesc, color = Color.Gray, fontSize = 20.sp)
  }
}

@Composable
fun NutritionalValue(title: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 13.dp),
    horizontalArrangement = Arrangement.Absolute.SpaceBetween
  ) {
    Text(text = title, fontSize = 16.sp, color = Color.Gray)
    Text(text = value, fontSize = 16.sp)
  }
}

@Preview
@Composable
fun ItemCardScreenPreview(modifier: Modifier = Modifier) {
  //ItemCardScreen(modifier = modifier)
}