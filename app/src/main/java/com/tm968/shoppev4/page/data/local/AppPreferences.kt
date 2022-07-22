package com.tm968.shoppev4.page.data.local

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securepreferences.SecurePreferences
import com.tm968.shoppev4.page.data.model.Cart
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.data.model.User

class AppPreferences(private val securePreferences: SecurePreferences) {
    companion object{
        const val TOKEN = "USER_TOKEN"
        const val USER_INFO = "USER_INFO"
        const val PRODUCT_LIST_INFO = "PRODUCT_LIST_INFO"
        const val LIST_PRODUCT_IN_CART = "LIST_PRODUCT_IN_CART"
    }

    fun setToken (token:String){
        securePreferences.edit().putString(TOKEN,token).apply()
    }
    fun getToken() = securePreferences.getString(TOKEN,null)

    fun setUserInfo(user:User){
        val gson = Gson()
        val json = gson.toJson(user)
        securePreferences.edit().putString(USER_INFO,json).apply()
    }
    fun getUser ():User{
        val gson = Gson()
        val string = securePreferences.getString(USER_INFO,null)

        return gson.fromJson(string,object: TypeToken<User>() {}.type)

    }

    fun setProductListInfo(productList:List<Product>){
        val gson = Gson()
        val json = gson.toJson(productList)
        securePreferences.edit().putString(PRODUCT_LIST_INFO,json).apply()


    }
    fun getProductListInfo():List<Product>{
        val gson = Gson()
        val string = securePreferences.getString(PRODUCT_LIST_INFO,null)

        return gson.fromJson(string,object:TypeToken<List<Product>>() {}.type)
    }

    fun addProductInCart(product:Product){
        val list = getListProductInCart()?.toMutableList()?: mutableListOf()
        list.add(product)
        val gson = Gson()
        val json = gson.toJson(list)
        securePreferences.edit().putString(LIST_PRODUCT_IN_CART,json).apply()
    }

    fun getListProductInCart():List<Product>?{
        val gson = Gson()
        val string = securePreferences.getString(LIST_PRODUCT_IN_CART,null)
        return gson.fromJson(string,object:TypeToken<List<Product>>() {}.type)


    }
    fun updateCart(listProductCart:MutableList<Cart>){
        securePreferences.edit().remove(LIST_PRODUCT_IN_CART).apply()
        val list = getListProductInCart()?.toMutableList()?: mutableListOf()
        for ( i in 0 until  listProductCart.size){
            list.add(listProductCart[i].product)
            }
        val gson = Gson()
        val json = gson.toJson(list)
        securePreferences.edit().putString(LIST_PRODUCT_IN_CART,json).apply()

    }
    fun clearCart(){
        securePreferences.edit().remove(LIST_PRODUCT_IN_CART).apply()
    }

    fun ClearAllData(){
        securePreferences.edit().remove(TOKEN).apply()
        securePreferences.edit().remove(USER_INFO).apply()
        securePreferences.edit().remove(LIST_PRODUCT_IN_CART).apply()
    }

}