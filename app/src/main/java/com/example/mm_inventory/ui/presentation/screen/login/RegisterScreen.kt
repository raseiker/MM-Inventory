package com.example.mm_inventory.ui.presentation.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.mm_inventory.ui.presentation.utils.MyButton
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldForm
import com.example.mm_inventory.ui.viewModel.user.UserViewModel

@Composable
fun RegisterScreen(
    userViewModel: UserViewModel,
    onRegisterClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userState by userViewModel.user.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        MyTextFieldForm(
            label = "Nombre",
            text = userState.name,
            onValueChange = { name -> userViewModel.onValueChanged(field = "name", value = name) },
            onClearText = { userViewModel.onClearText(field = "name") },
            keyboardType = KeyboardType.Text,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Correo",
            text = userState.email,
            onValueChange = { email ->
                userViewModel.onValueChanged(
                    field = "email",
                    value = email
                )
            },
            onClearText = { userViewModel.onClearText(field = "email") },
            keyboardType = KeyboardType.Email,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Contraseña",
            text = userState.password,
            onValueChange = { pass ->
                userViewModel.onValueChanged(
                    field = "password",
                    value = pass
                )
            },
            onClearText = { userViewModel.onClearText(field = "password") },
            keyboardType = KeyboardType.NumberPassword,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyTextFieldForm(
            label = "Repetir contraseña",
            text = userViewModel.confirmPass.value,
            onValueChange = { confirmPass ->
                userViewModel.onValueChanged(
                    field = "confirmPass",
                    value = confirmPass
                )
            },
            onClearText = { userViewModel.onClearText(field = "confirmPass") },
            keyboardType = KeyboardType.NumberPassword,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        MyButton(
            text = "Registrarme",
            onClick = {
                userViewModel.addUserTest()
                onRegisterClicked()
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 30.dp)
        )
    }
}