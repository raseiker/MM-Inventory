package com.example.mm_inventory.ui.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mm_inventory.R
import com.example.mm_inventory.model.data.product.ProductState
import com.example.mm_inventory.model.data.response.Response
import com.example.mm_inventory.ui.presentation.utils.MyNormalCard
import com.example.mm_inventory.ui.presentation.utils.MyProgressBar
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldForm
import com.example.mm_inventory.ui.viewModel.product.ProductViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    productViewModel: ProductViewModel,
    onCardClicked: (String) -> Unit,
    onDoChangedClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listProductState by productViewModel.products.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        MyTextFieldForm(
            label = "Filtrar",
            text = productViewModel.filterText.value,
            onValueChange = { productViewModel.onValueChanged(field = "filter", value = it) },
            onClearText = { productViewModel.onClearText(field = "filter") },
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
            onSearchClicked = { productViewModel.getProductByName(name = productViewModel.filterText.value)},
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
//            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = listProductState) { product ->
                MyNormalCard(
                    title = product.name,
                    subTitle = "${product.stock} unidades",
                    stock = product.stock.toInt(),
                    icon = R.drawable.ic_baseline_local_drink_24,
                    onClick = {
                        productViewModel.getProductByIdTest2(idProduct = product.idProduct)
                        onCardClicked(product.idProduct)
                    },//change to idProduct
                    onDeleteClicked = {
//                                productViewModel.deleteProduct(idProduct = product.idProduct).getProducts()
                        productViewModel.deleteProductTest(idProduct = product.idProduct)
                    },
                    onOperateExistenceClicked = {
                        productViewModel.getProductByIdTest2(idProduct = product.idProduct)
                        onDoChangedClicked()
                    },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                )
            }
        }

        if (productViewModel.isLoading.value) MyProgressBar()

//        when (val response = productViewModel.productsTest.value) {
//            is Response.Success -> {
//                LazyColumn(
////            verticalArrangement = Arrangement.spacedBy(10.dp)
//                ) {
//                    items(items = response.data) { product ->
//                        MyNormalCard(
//                            title = product.name,
//                            subTitle = "${product.stock} unidades",
//                            icon = R.drawable.ic_baseline_local_drink_24,
//                            onClick = {
//                                productViewModel.getProductByIdTest2(idProduct = product.idProduct)
//                                onCardClicked(product.idProduct)
//                                      },//change to idProduct
//                            onDeleteClicked = {
////                                productViewModel.deleteProduct(idProduct = product.idProduct).getProducts()
//                                productViewModel.deleteProductTest(idProduct = product.idProduct)
//                            },
//                            onOperateExistenceClicked = {
//                                productViewModel.getProductByIdTest2(idProduct = product.idProduct)
//                                onDoChangedClicked()
//                                                        },
//                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
//                        )
//                    }
//                }
//            }
//            is Response.Error -> Unit
//            is Response.Loading -> MyProgressBar()
//        }

    }
}