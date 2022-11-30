package com.example.mm_inventory.ui.presentation.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mm_inventory.ui.presentation.utils.*
import com.example.mm_inventory.ui.viewModel.product.ProductViewModel
import com.example.mm_inventory.ui.viewModel.user.UserViewModel

@Composable
fun LoginScreen(
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel,
    onLoginClicked: () -> Unit,
    onRegisterClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val userState by userViewModel.user.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        MyLogo(
            modifier = Modifier
                .padding(top = 80.dp, bottom = 30.dp)
                .align(Alignment.CenterHorizontally)
        )

        MyTextFieldForm(
            label = "Correo",
            text = userState.email,
            onValueChange = {email -> userViewModel.onValueChanged(field = "email", value = email)},
            onClearText = { userViewModel.onClearText(field = "email") },
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Contraseña",
            text = userState.password,
            onValueChange = {pass -> userViewModel.onValueChanged(field = "password", value = pass)},
            onClearText = { userViewModel.onClearText(field = "password") },
            keyboardType = KeyboardType.NumberPassword,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyButton(
            text = "Ingresar",
            onClick = {
                userViewModel.signIn()
                if (userViewModel.isUserAutheticated.value) onLoginClicked()
                      },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        )

        MyForgivenPassword(
            onRegisterClicked = onRegisterClicked,
            modifier = Modifier.padding(vertical = 20.dp)
        )
    }
    if (productViewModel.isLoading.value) MyProgressBar()
}