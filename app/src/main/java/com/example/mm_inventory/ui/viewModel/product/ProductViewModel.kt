package com.example.mm_inventory.ui.viewModel.product

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mm_inventory.model.data.product.ProductState
import com.example.mm_inventory.model.data.response.Response
import com.example.mm_inventory.model.repo.product.ProductRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val repo = ProductRepo()
    private var _products = MutableStateFlow(listOf<ProductState>())
    val products = _products.asStateFlow()
    private var _product = MutableStateFlow(ProductState())
    val product = _product.asStateFlow()
    var existenceOperator = mutableStateOf("")
    var existenceQuantity = mutableStateOf("")
    var isAble = mutableStateOf(true)
        private set
    var isLoading = mutableStateOf(true)
        private set
    var filterText = mutableStateOf("")
        private set

    private val productsTest = mutableStateOf<Response<List<ProductState>>>(Response.Loading)

    init {
//        getProducts()
//        getProductsTest()
        getAllProductTest()
//        Log.d("addPro", "init ProductviewModel")
    }

    fun onValueChanged(field: String, value: String) {
        when (field) {
            "name" -> _product.update { it.copy(name = value) }
            "supplier" -> _product.update { it.copy(supplier = value) }
            "category" -> _product.update { it.copy(category = value) }
            "operator" -> existenceOperator.value = value
            "filter" -> filterText.value = value.lowercase().trim()
            "stock" -> _product.update { it.copy(stock = value.filter { it.isDigit() }) }
            "purchase" -> _product.update { it.copy(purchaseBuy = value) }
            "sale" -> _product.update { it.copy(saleBuy = value) }
            "quantity" -> {
                existenceQuantity.value = value
                isAble.value = !(existenceOperator.value == "Salida" &&
                        _product.value.stock.toInt() - (if (existenceQuantity.value.isBlank()) 0 else existenceQuantity.value.toInt()) < 0)
            }
        }
    }

    fun onClearText(field: String): ProductViewModel {
        when (field) {
            "name" -> _product.update { it.copy(name = "") }
            "supplier" -> _product.update { it.copy(supplier = "") }
            "stock" -> _product.update { it.copy(stock = "0") }
            "purchase" -> _product.update { it.copy(purchaseBuy = "0.0") }
            "sale" -> _product.update { it.copy(saleBuy = "0.0") }
            "all" -> _product.update { ProductState() }
            "operator" -> existenceOperator.value = ""
            "quantity" -> existenceQuantity.value = ""
            "filter" -> filterText.value = "".also { getAllProductTest() }
        }
        return this
    }

    fun addProduct(): ProductViewModel {
        viewModelScope.launch {
            val newRef = db.collection("products").document()
            _product.update { it.copy(idProduct = newRef.id) }
            newRef.set(_product.value)
                .addOnSuccessListener { docRef ->
                    Log.d("addPro", "DocumentSnapshot added with ID: ${docRef}")
                }
                .addOnFailureListener { e ->
                    Log.w("addPro", "Error", e)
                }
        }
        return this
    }

    fun getProducts(): ProductViewModel {
        viewModelScope.launch {
            db.collection("products").get()
                .addOnSuccessListener { result ->
                    if (result.documents.size == 0) {
                        Log.d("readPro", "Empty list...")
                    } else {
                        Log.d("readPro", "size list... ${result.documents.size}")
                        val listProduct =
                            result.documents.map { it.toObject(ProductState::class.java)!! }
                        _products.update { listProduct }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("readPro", "Error", e)
                }
        }
        return this
    }

    fun deleteProduct(idProduct: String): ProductViewModel {
        viewModelScope.launch {
            db.collection("products").document(idProduct).delete()
                .addOnSuccessListener {
                    Log.d("deletePro", "document deleted")
                }
                .addOnFailureListener { e ->
                    Log.w("deletePro", "Error", e)
                }
        }
        return this
    }

    fun updateProduct(idProduct: String): ProductViewModel {
        viewModelScope.launch {
            db.collection("products").document(idProduct)
//                .update()
        }
        return this
    }

    private fun getProductsTest() = viewModelScope.launch {
        repo.getAllProduct().collect { incomingList ->
            productsTest.value = incomingList
        }
    }

    fun addProductTest() = viewModelScope.launch {
        isLoading.value = true
        repo.addProduct(_product.value)
    }

    fun deleteProductTest(idProduct: String) = viewModelScope.launch {
        isLoading.value = true
        repo.deleteProduct(idProduct = idProduct)
    }

    fun updateProductTest(productState: ProductState) = viewModelScope.launch {
        isLoading.value = true
        repo.updateProduct(productState = productState)
    }

    fun getProductByIdTest(idProduct: String) = viewModelScope.launch {
        when (val res = repo.getProductById(idProduct = idProduct)) {
            is Response.Success -> _product.update { res.data }
            is Response.Loading -> Unit
            is Response.Error -> Unit
        }
    }

    fun getProductByIdTest2(idProduct: String) = viewModelScope.launch {
//        when(val res = productsTest.value){
//            is Response.Success -> {
//                _product.update { res.data.first { it.idProduct == idProduct} }
//            }
//            is Response.Error -> Log.d("findPro", "document dont exists")
//            is Response.Loading -> Log.d("findPro", "document loading...")
//        }
        isLoading.value = true
        _product.update { _products.value.first { it.idProduct == idProduct } }
        isLoading.value = false
    }

    fun doOperatorExistence(idProduct: String, newValue: Int) = viewModelScope.launch {
        isLoading.value = true
        val newStock = if (existenceOperator.value == "Salida"){
            _product.value.stock.toInt() - newValue
        } else {
            _product.value.stock.toInt() + newValue
        }
        when (val res = repo.updateStockById(idProduct = idProduct, newValue = newStock.toString())){
            is Response.Error -> Log.d("updateStock", "error... ${res.e}")
            Response.Loading -> TODO()
            is Response.Success -> Log.d("updateStock", "updated is... ${res.data}")
        }
        onClearText(field = "quantity").onClearText(field = "operator")
    }

    private fun getAllProductTest() = viewModelScope.launch {
        repo.getAllProductTest().collect { res ->
            when (res) {
                is Response.Error -> Log.d("readProTest", "error... ${res.e}")
                Response.Loading -> Log.d("readProTest", "loading...")
                is Response.Success -> {
                    _products.update { res.data }
                    isLoading.value = false
                    Log.d("readProTest", "size data... ${res.data.size}")
                }
            }
        }
    }

    fun getProductByName(name: String) = viewModelScope.launch {
        isLoading.value = true
        when (val res = repo.getProductByName(name = name)) {
            is Response.Error ->  Log.w("getProName", "Error... ${res.e}")
            Response.Loading -> Unit
            is Response.Success -> {
                Log.d("getProName", "document is... ${res.data.stock}")
                _products.update { it - it + res.data }
            }
        }
//        val oldList = _products.value
//        val searchedList = oldList.filter { it.name == name }
//        _products.update { searchedList.ifEmpty { it } }
    }




}