package com.tm968.shoppev4.page.repo

import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.base.ResponseError
import com.tm968.shoppev4.page.data.model.*
import com.tm968.shoppev4.page.data.remote.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody

class Repository(private val api: Api) {

    suspend fun login(Account:String,Password:String): BaseResponse<User> {
        return try{
            BaseResponse.Success(api.login(Account,Password))
        }
        catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101, ex.message.toString()))
        }
    }
    suspend fun register(Account: String
                         , Password: String
                         , Email:String
                         ,Phone:String,fullName:String): BaseResponse<Register>{
        return try{
            BaseResponse.Success(api.register(Account,Password,Email,Phone,fullName))
        }
        catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun logout(): BaseResponse<Unit> {
        return try{
            BaseResponse.Success(api.logout())
        }
        catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun getProductList():BaseResponse<List<Product>>{
        return try{
            BaseResponse.Success(api.getProductList())
        }
        catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun getProductListShowView(): BaseResponse<List<Product>>{
        return try{
            BaseResponse.Success(api.getProductListShowView())
        }
        catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun getProductSearch(key:String):BaseResponse<List<Product>>{
        return try{
            BaseResponse.Success(api.getSearchProduct(key))
        }catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun createOrder(orderRequest: OrderRequest):BaseResponse<String>{
        return try {
            BaseResponse.Success(api.createOrder(orderRequest))
        }catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun getListOrder(): BaseResponse<List<Order>>{
        return try{
            BaseResponse.Success(api.getListOrder())
        }catch (ex:Exception){
            return BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun createProduct(name:RequestBody,
                              image:MultipartBody.Part,
                              price:RequestBody,
                              salePrice:RequestBody,
                              categoryId:RequestBody,
                              description:RequestBody):BaseResponse<String>{
        return try{
            BaseResponse.Success(api.createProduct(name,image,price,salePrice,categoryId,description))
        } catch (ex:Exception){
                BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }
    suspend fun editProduct(id:Int
                            ,name:String
                            ,image_url:String
                            ,price:Double
                            ,salePrice: Double
                            ,categoryId:Int
                            ,view:Int
                            ,description: String):BaseResponse<String>{
        return try {
            BaseResponse.Success(api.editProduct(id,name,image_url,price,salePrice,categoryId,view,description))
        }
        catch (ex:Exception){
            BaseResponse.Error(ResponseError(101,ex.message.toString()))
        }
    }


}