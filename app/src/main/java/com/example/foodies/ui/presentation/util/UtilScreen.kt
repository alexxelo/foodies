package com.example.foodies.ui.presentation.util


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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

@Preview
@Composable
fun SplashScreenPreview(modifier: Modifier = Modifier) {
  SplashScreen(modifier = modifier)
}