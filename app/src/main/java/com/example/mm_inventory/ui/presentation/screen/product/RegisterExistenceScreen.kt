package com.example.mm_inventory.ui.presentation.screen.product

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mm_inventory.model.data.product.ProductState
import com.example.mm_inventory.ui.presentation.utils.MyButton
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldForm
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldMenu
import com.example.mm_inventory.ui.viewModel.product.ProductViewModel

@Composable
fun RegisterExistenceScreen(
    productViewModel: ProductViewModel,
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit
){
    val productState by productViewModel.product.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        MyTextFieldForm(
            label = "Stock actual",
            text = productState.stock,
            onValueChange = {},
            onClearText = {},
            keyboardType = KeyboardType.Number,
            isReadOnly = true,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldMenu(
            label = "Operación",
            items = listOf("Salida", "Entrada"),
            text = productViewModel.existenceOperator.value,
            onValueChange = { productViewModel.onValueChanged(field = "operator", value = it) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Cantidad",
            text = productViewModel.existenceQuantity.value,
            onValueChange = { productViewModel.onValueChanged(field = "quantity", value = it) },
            onClearText = { productViewModel.onClearText(field = "quantity") },
            keyboardType = KeyboardType.Number,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyButton(
            text = "Aplicar",
            enabled = productViewModel.isAble.value,
            onClick = {
                productViewModel.doOperatorExistence(idProduct = productState.idProduct, newValue = productViewModel.existenceQuantity.value.toInt())
                onButtonClicked()
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        )
    }
}