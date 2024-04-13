package com.example.foodies.ui.presentation.catalog


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CatalogScreen(
  modifier: Modifier = Modifier,
  viewModel: CatalogViewModel
) {

  Column(modifier = modifier.fillMaxSize().background(Color.Green)) {

    Text(text =  "tags - ${viewModel.tag.value}")
    Text(text =  "category - ${viewModel.category.value}")
    Text(text =  "prod - ${viewModel.product.value}")
  }
}

@Preview
@Composable
fun CatalogScreenPreview(modifier: Modifier = Modifier) {
  //CatalogScreen(modifier = modifier)
}
