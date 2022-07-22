package com.tm968.shoppev4.page.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/** Utility function used to read input file into a byte array */
fun loadInputBuffer(filePath :String): ByteArray {
    val inputFile = File(filePath)
    return BufferedInputStream(inputFile.inputStream()).let { stream ->
        ByteArray(stream.available()).also {
            stream.read(it)
            stream.close()
        }
    }
}
suspend fun getBitmap(path: String ,coroutineScope: CoroutineScope): Bitmap? = suspendCoroutine { result->
    try {
        coroutineScope.launch(Dispatchers.IO){
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            val bitmap = BitmapFactory.decodeFile(path, options)
            result.resume(bitmap)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        result.resume(null)
    }
}

//fun rotateImage(source: Bitmap, angle: Int): Bitmap? {
//    val matrix = Matrix()
//    matrix.postRotate(angle.toFloat())
//    return Bitmap.createBitmap(
//        source, 0, 0, source.width, source.height,
//        matrix, true
//    )
//}

fun scaleBitmap(context: Context, rotatedBitmap: Bitmap): Bitmap {
    //get height and width of device
    val displayMetrics = context.resources.displayMetrics
    //get new width following ratio of screen
    var width = displayMetrics.widthPixels
    width = width * 7 / 10   // 7/10 ratio of screen
    var height = width  // height = width => we have square
    // => we have width of final bitmap
    // we take height multiplied ratio of original bitmap
    // rotatedBitmap.height / rotatedBitmap.width is ratio of height in original bitmap size
    //Ex : ratio of height = height/width
    height = height * rotatedBitmap.height / rotatedBitmap.width
    return Bitmap.createScaledBitmap(rotatedBitmap, width, height, true)
}

suspend fun geImageCompressed(
    defaultWidth :Int,
    defaultHeight :Int,
    filePath: String,
    context: Context,
    coroutineScope: CoroutineScope
): File
        = suspendCoroutine { result ->
    coroutineScope.launch(Dispatchers.IO){
        val file = File(filePath)
        var width = defaultWidth
        var height = defaultHeight
        when(defaultHeight.toFloat()/defaultWidth.toFloat()){
            16F/9F -> {
                height = 1920
                width = 1080
            }
            9F/16 -> {
                height = 1080
                width = 1920
            }
            4F/3F ->{
                width = 1512
                height = 2016
            }
            3F/4F -> {
                width = 2016
                height = 1512
            }
        }
        result.resume(
            Compressor.compress(
            context, file) {
            format(Bitmap.CompressFormat.JPEG)
            default(width,height)
            resolution(width,height)
            size(2_097_152)
            quality(50)}
        )
    }
}

fun getBitmapWithExif(inputStream: ByteArrayInputStream): Bitmap {
    //this code to save inputStream to sourceStream
    val sourceStream = ByteArrayOutputStream()
    val buffer = ByteArray(1024)
    var len: Int
    while (inputStream.read(buffer).also { len = it } > -1) {
        sourceStream.write(buffer, 0, len)
    }
    sourceStream.flush()
    // Open new InputStreams using recorded bytes
    // Can be repeated as many times as you wish
    val is1 = ByteArrayInputStream(sourceStream.toByteArray())
    val is2 = ByteArrayInputStream(sourceStream.toByteArray())
    val bm = BitmapFactory.decodeStream(is1)
    val ei = ExifInterface(is2) // get Exif Info
    val orientation = ei.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    ) // get orientation info via attribute tag
    val bitmapTransformation = decodeExifOrientation(orientation)
    return Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, bitmapTransformation, true)
}