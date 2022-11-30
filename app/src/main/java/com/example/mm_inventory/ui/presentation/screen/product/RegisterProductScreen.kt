package com.example.mm_inventory.ui.presentation.screen.product

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mm_inventory.ui.presentation.utils.MyButton
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldForm
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldMenu
import com.example.mm_inventory.ui.viewModel.product.ProductViewModel

@Composable
fun RegisterProductScreen(
    productViewModel: ProductViewModel,
    onAddProductClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val productState by productViewModel.product.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        MyTextFieldForm(
            label = "Nombre",
            text = productState.name,
            onValueChange = { productViewModel.onValueChanged(field = "name", value = it) },
            onClearText = { productViewModel.onClearText(field = "name") },
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Proveedor",
            text = productState.supplier,
            onValueChange = { productViewModel.onValueChanged(field = "supplier", value = it) },
            onClearText = { productViewModel.onClearText(field = "supplier") },
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            MyTextFieldMenu(
                label = "Categoría",
                items = listOf("Industrial", "Casero"),
                text = productState.category,
                onValueChange = { productViewModel.onValueChanged(field = "category", value = it) },
                modifier = Modifier.weight(1f)
            )
            MyTextFieldForm(
                label = "Stock",
                text = productState.stock,
                onValueChange = { productViewModel.onValueChanged(field = "stock", value = it) },
                onClearText = { productViewModel.onClearText(field = "stock") },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            MyTextFieldForm(
                label = "Precio de compra",
                text = productState.purchaseBuy,
                onValueChange = { productViewModel.onValueChanged(field = "purchase", value = it) },
                onClearText = { productViewModel.onClearText(field = "purchase") },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )
            MyTextFieldForm(
                label = "Precio de venta",
                text = productState.saleBuy,
                onValueChange = { productViewModel.onValueChanged(field = "sale", value = it) },
                onClearText = { productViewModel.onClearText(field = "sale") },
                keyboardType = KeyboardType.Number,
                modifier = Modifier.weight(1f)
            )
        }

        MyButton(
            text = "Agregar",
            onClick = {
//                productViewModel.addProduct().getProducts().onClearText(field = "all")
                productViewModel.addProductTest()
                onAddProductClicked()
//                productViewModel.onClearText(field = "all")
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        )
    }
}