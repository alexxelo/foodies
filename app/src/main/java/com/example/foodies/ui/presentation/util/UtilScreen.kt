package com.example.foodies.ui.presentation.util


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.foodies.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SplashScreen(modifier: Modifier = Modifier, onHomePage: () -> Unit = {}) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFFF15412)),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    GlideImage(model = R.drawable.animation_640_l4ecga8j_1, contentDescription = "")
  }
  LaunchedEffect(key1 = true) {
    delay(3500L)
    onHomePage()
  }
}


@Composable
fun EmptyScreen(modifier: Modifier = Modifier, text: String) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = text)
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

@Preview
@Composable
fun SplashScreenPreview(modifier: Modifier = Modifier) {
  SplashScreen(modifier = modifier)
}