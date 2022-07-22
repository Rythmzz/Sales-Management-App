package com.tm968.shoppev4.page.data.remote
import com.tm968.shoppev4.page.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface Api {
    // Login
    @FormUrlEncoded
    @POST("api/Authentication/login")
    suspend fun login(
        @Field("userName") Account:String,
        @Field("password") Password:String) : User

    // Register
    @FormUrlEncoded
    @POST("api/Authentication/register")
    suspend fun register(
        @Field ("userName")Account: String,
        @Field("password") Password: String,
        @Field ("Email") Email:String,
        @Field("phoneNumber") phone:String,
        @Field("fullName") fullName:String) : Register

    // Logout
    @POST("api/Authentication/logout")
    suspend fun logout()

    //GetProductList
    @GET("api/Products")
    suspend fun getProductList():List<Product>

    // GetProductListSortByView
    @GET("api/Products/get-product-by-view")
    suspend fun getProductListShowView():List<Product>

    // GetSearchProduct
    @GET("findProducts/Products")
    suspend fun getSearchProduct(@Query("product") product:String):List<Product>

    // Create A Order
    @POST("api/Order/create")
    suspend fun createOrder(
        @Body body: OrderRequest
    ):String

    // Get List Order
    @GET("api/Order/get-orders")
    suspend fun getListOrder():List<Order>

    // Edit Product
    @FormUrlEncoded
    @POST("api/Products/edit")
    suspend fun editProduct(
        @Field("id") id:Int,
        @Field("name")name :String,
        @Field("image_url") imageUrl:String,
        @Field("price") price:Double,
        @Field("sale_price") salePrice: Double,
        @Field("categoryId") category: Int,
        @Field("view") view:Int,
        @Field("description") description: String
    ):String


    // Create Product
    @Multipart
    @POST("api/Products/image")
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part image: MultipartBody.Part,
        @Part ("price") price: RequestBody,
        @Part ("sale_price") salePrice: RequestBody,
        @Part ("categoryId") categoryId: RequestBody,
        @Part ("description") description:RequestBody
    ):String
}