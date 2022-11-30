package com.example.mm_inventory.model.repo.product

import android.util.Log
import com.example.mm_inventory.model.data.product.ProductState
import com.example.mm_inventory.model.data.response.Response
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class ProductRepo {
    private val db = Firebase.firestore
    private val PRODUCT_COLLECTION = "products"

    fun getAllProduct(): Flow<Response<List<ProductState>>> = callbackFlow {
       val snap = db.collection(PRODUCT_COLLECTION).orderBy("stock")
            .addSnapshotListener { result, e ->
                val response = if (result == null) {
                    Log.w("readPro", "Error", e)
                    Response.Error(e = e.toString())
                } else {
                    Log.d("readPro", "repo. size list... ${result.documents.size}")
                    val listProduct = result.documents.map { it.toObject(ProductState::class.java)!! }
                    Response.Success(data = listProduct)
                }
                trySend(response)
            }
        awaitClose { snap.remove() }
//            .addOnSuccessListener { result ->
//                val listProduct = result.documents.map { it.toObject(ProductState::class.java)!! }
//                Response.Success(data = listProduct)
//            }
//            .addOnFailureListener { e ->
//                Log.w("readPro", "Error", e)
//                Response.Error(e = e.toString())
//            }
    }

    suspend fun deleteProduct(idProduct: String): Response<Boolean> {
        return try {
            db.collection(PRODUCT_COLLECTION).document(idProduct).delete().await()
            Log.d("deletePro", "document deleted")
            Response.Success(data = true)
        } catch (e: Exception) {
            Log.d("deletePro", "error.. $e")
            Response.Error(e = e.toString())
        }
    }

    suspend fun addProduct(productState: ProductState): Response<Boolean> {
        return try {
            val newRef = db.collection(PRODUCT_COLLECTION).document()
            newRef.set(productState.copy(idProduct = newRef.id)).await()
            Log.d("addPro", "document added")
            Response.Success(data = true)
        } catch (e: Exception) {
            Log.w("addPro", "Error", e)
            Response.Error(e = e.toString())
        }
    }

    suspend fun updateProduct(productState: ProductState): Response<Boolean> {
        return try {
            db.collection(PRODUCT_COLLECTION).document(productState.idProduct)
                .set(productState).await()
            Log.d("updatePro", "document updated... ${productState.name}")
            Response.Success(data = true)
        } catch (e: Exception) {
            Log.w("updatePro", "Error", e)
            Response.Error(e = e.toString())
        }
    }

    suspend fun getProductById(idProduct: String): Response<ProductState> {
        return try {
            val response = db.collection(PRODUCT_COLLECTION).document(idProduct)
                .get().await().toObject(ProductState::class.java)!!
            Log.d("getPro", "document is... ${response.name}")
            Response.Success(data = response)
        } catch (e: Exception) {
            Log.w("getPro", "Error", e)
            Response.Error(e = e.toString())
        }
    }

    fun getAllProductTest(): Flow<Response<List<ProductState>>> {
        return db.collection(PRODUCT_COLLECTION).orderBy("name", Query.Direction.ASCENDING)
            .snapshots()
            .map { doc -> Response.Success(data = doc.toObjects(ProductState::class.java))
            }
            .catch { Response.Error(it.toString()) }
    }

    suspend fun updateStockById(idProduct: String, newValue: String): Response<Boolean>{
        return try {
            db.collection(PRODUCT_COLLECTION).document(idProduct)
                .update("stock", newValue)
                .await()
                Response.Success(data = true)
        } catch (e: Exception){
            Response.Error(e = e.toString())
        }
    }

    suspend fun getProductByName(name: String): Response<ProductState> {
        return try {
            val response = db.collection(PRODUCT_COLLECTION).whereEqualTo("name", name)
                .get().await().map { it.toObject(ProductState::class.java)}.first()
            Response.Success(data = response)
        } catch (e: Exception) {
            Response.Error(e = e.toString())
        }
    }

    fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { value, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (value != null)
                trySend(value)
//                Log.d("readPro", "repo. size list... ${value?.documents?.size}")
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

}