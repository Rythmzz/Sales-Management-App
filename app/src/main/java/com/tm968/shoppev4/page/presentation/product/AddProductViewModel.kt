package com.tm968.shoppev4.page.presentation.product

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.base.ResponseError
import com.tm968.shoppev4.page.repo.Repository
import com.tm968.shoppev4.page.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AddProductViewModel(private val repository: Repository):ViewModel() {
    private val _loading = MutableSharedFlow<Boolean>()
    val loading :SharedFlow<Boolean> get() = _loading

    private val _success = MutableSharedFlow<Any>()
    val success: SharedFlow<Any> get() = _success

    fun createProduct(name:String,fileImagePicker:File,price:String,salePrice:String,typeOfProduct:Int,description:String){
        viewModelScope.launch {
            _loading.emit(true)
            val requestFile =fileImagePicker.asRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val imageBody = MultipartBody.Part.createFormData("image",fileImagePicker.name,requestFile)
            val nameBody =name.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val priceBody = price.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val saleBody = salePrice.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            val productTypeBody = (typeOfProduct+1).toString().toRequestBody(MULTIPART_FORM_DATA.toMediaTypeOrNull())
            when (val Result = repository.createProduct(nameBody,imageBody,priceBody,saleBody,productTypeBody,descriptionBody)){
                is BaseResponse.Success -> {
                    _success.emit(true)
                    _loading.emit(false)
                }
                is BaseResponse.Error -> {
                    _success.emit((Result.exception as ResponseError).msg)
                    _loading.emit(false)
                }
                BaseResponse.Loading -> Unit
            }


        }

    }
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getImageLocalFile(context: Context, imageURI:Uri):File = suspendCoroutine { result ->
            viewModelScope.launch(Dispatchers.IO) {
                _loading.emit(true)
                var file = createFile(
                    checkFolderIsExistsIfNoteCreateOne(
                        filePath =context.cacheDir.absolutePath,
                        folderName = TEMP_FOLDER_NAME),
                        FILE_NAME_FORMAT,
                        PHOTO_EXTENSION,
                    context.resources.configuration.locales[0])
                    context.contentResolver.openInputStream(imageURI)?.let { inputStream ->
                        file = getFileFromInputStream(file, inputStream)


                        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        val imageFile = geImageCompressed(
                            bitmap.width,
                            bitmap.height,
                            file.absolutePath,
                            context,
                            this
                        )
                    }
                _loading.emit(false)
                result.resume(file)

            }

    }

}