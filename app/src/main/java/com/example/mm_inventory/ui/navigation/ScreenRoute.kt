package com.example.mm_inventory.ui.navigation

sealed class ScreenRoute(
    val route: String,
    val title: String? = null
){
    object LoginScreen: ScreenRoute(route = "login")
    object RegisterScreen: ScreenRoute(route = "register", title = "Registro")
    object RegisterExistenceScreen: ScreenRoute(route = "register_existence/", title = "Registrar Existencias")
    object RegisterProductScreen: ScreenRoute(route = "register_product", title = "Agregar producto")
    object UpdateProductScreen: ScreenRoute(route = "update_product/", title = "Detalle producto")
    object HomeScreen: ScreenRoute(route = "home", title = "MM Inventory")

}
