package com.example.mm_inventory.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mm_inventory.R
import com.example.mm_inventory.model.data.product.ProductState
import com.example.mm_inventory.ui.presentation.screen.HomeScreen
import com.example.mm_inventory.ui.presentation.screen.login.LoginScreen
import com.example.mm_inventory.ui.presentation.screen.login.RegisterScreen
import com.example.mm_inventory.ui.presentation.screen.product.RegisterExistenceScreen
import com.example.mm_inventory.ui.presentation.screen.product.RegisterProductScreen
import com.example.mm_inventory.ui.presentation.screen.product.UpdateProductScreen
import com.example.mm_inventory.ui.presentation.utils.MyFab
import com.example.mm_inventory.ui.presentation.utils.MyTextFieldForm
import com.example.mm_inventory.ui.presentation.utils.MyTopApBar
import com.example.mm_inventory.ui.theme.MMInventoryTheme
import com.example.mm_inventory.ui.viewModel.product.ProductViewModel
import com.example.mm_inventory.ui.viewModel.user.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MyNavHost(
    navController: NavHostController = rememberNavController(),
    userViewModel: UserViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel()
) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentDestination = backStackEntry?.destination?.route ?: ScreenRoute.LoginScreen.route
    MMInventoryTheme(darkTheme = false) {
        Scaffold(
            topBar = {
                if (currentDestination != ScreenRoute.LoginScreen.route) {
                    MyTopApBar(
                        title = if (currentDestination == ScreenRoute.HomeScreen.route) null else getCurrentTitleScreen(currentDestination),
                        navIcon = if (currentDestination != ScreenRoute.HomeScreen.route) ImageVector.vectorResource(id = R.drawable.ic_baseline_arrow_back_24) else null,
                        onActionIcon = {
                            userViewModel.userOut().onClearText(field = "all")
                            if(userViewModel.isUserAutheticated.value) navController.navigate(route = ScreenRoute.LoginScreen.route)
                                       },
                        actionIcon = if (currentDestination == ScreenRoute.HomeScreen.route) ImageVector.vectorResource(id = R.drawable.power_off_solid)
                        else null,
                        searchField = {
                            MyTextFieldForm(
                                label = "Filtrar por nombre...",
                                text = productViewModel.filterText.value,
                                onValueChange = { productViewModel.onValueChanged(field = "filter", value = it) },
                                onClearText = { productViewModel.onClearText(field = "filter") },
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search,
                                onSearchClicked = { productViewModel.getProductByName(name = productViewModel.filterText.value)},
                                modifier = Modifier.offset(x = (-50).dp).fillMaxWidth(),
                                isSearchBar = true
                            )
                        }
                    )
                }
            },
            floatingActionButton = {
                if (currentDestination == ScreenRoute.HomeScreen.route) {
                    MyFab(
                        onClick = {
                            if (productViewModel.product.value != ProductState()) productViewModel.onClearText(field = "all")
                            navController.navigate(route = ScreenRoute.RegisterProductScreen.route)
                        }
                    )
                }
            }
        ) { padval ->
            NavHost(
                navController = navController,
                startDestination = if(userViewModel.isUserAutheticated.value) ScreenRoute.HomeScreen.route else ScreenRoute.LoginScreen.route,//should be splash screen
            ) {
                composable(route = ScreenRoute.LoginScreen.route) {
                    LoginScreen(
                        userViewModel = userViewModel,
                        productViewModel = productViewModel,
                        onLoginClicked = { navController.navigate(route = ScreenRoute.HomeScreen.route) },
                        onRegisterClicked = { navController.navigate(route = ScreenRoute.RegisterScreen.route) },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

                composable(route = ScreenRoute.RegisterScreen.route) {
                    RegisterScreen(
                        userViewModel = userViewModel,
                        onRegisterClicked = { navController.navigate(route = ScreenRoute.HomeScreen.route) },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

                composable(route = ScreenRoute.HomeScreen.route) {
                    HomeScreen(
                        productViewModel = productViewModel,
                        onCardClicked = { idProduct -> navController.navigate(route = ScreenRoute.UpdateProductScreen.route + idProduct) },
                        onDoChangedClicked = { navController.navigate(route = ScreenRoute.RegisterExistenceScreen.route) },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

                composable(
                    route = ScreenRoute.UpdateProductScreen.route + "{idProduct}",
                    arguments = listOf(navArgument(name = "idProduct"){
                        type = NavType.StringType
                    })
                ) {
                    UpdateProductScreen(
                        productViewModel = productViewModel,
                        idProduct = it.arguments?.getString("idProduct") ?: "nothing",
                        onUpdateProductClicked = { navController.navigateUp() },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

                composable(route = ScreenRoute.RegisterExistenceScreen.route) {
                    RegisterExistenceScreen(
                        productViewModel = productViewModel,
                        onButtonClicked = { navController.navigateUp() },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

                composable(route = ScreenRoute.RegisterProductScreen.route) {
                    RegisterProductScreen(
                        productViewModel = productViewModel,
                        onAddProductClicked = { navController.navigateUp() },
                        modifier = Modifier.padding(paddingValues = padval)
                    )
                }

            }

        }
    }
}

fun getCurrentTitleScreen(currentDestination: String): String {
    return when (currentDestination) {
        ScreenRoute.RegisterProductScreen.route -> ScreenRoute.RegisterProductScreen.title.toString()
        ScreenRoute.HomeScreen.route -> ScreenRoute.HomeScreen.title.toString()
        ScreenRoute.RegisterExistenceScreen.route -> ScreenRoute.RegisterExistenceScreen.title.toString()
        ScreenRoute.RegisterScreen.route -> ScreenRoute.RegisterScreen.title.toString()
        ScreenRoute.UpdateProductScreen.route -> ScreenRoute.UpdateProductScreen.title.toString()
        else -> "Detalle producto"
    }
}