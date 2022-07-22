package com.tm968.shoppev4.page.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.util.Log
import android.webkit.MimeTypeMap
import com.tm968.shoppev4.R
import java.io.*
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


/**
 * Create a [File] named a using formatted timestamp with the current date and time.
 *
 * @return [File] created.
 */
fun createFile(folderPath: String,format:String, extension: String,locale: Locale): File {
    val sdf = SimpleDateFormat(format, locale)
    return when(extension){
        PHOTO_EXTENSION -> File(folderPath, "IMG_${sdf.format(Date())}.$extension")
        else -> File(folderPath, "VID_${sdf.format(Date())}.$extension")
    }
}

fun getSizeOfFileUnit(unit:String,size:Double):Double{
    val newSize = when(unit){
        KILOBYTE_UNIT -> size/1024
        MEGABYTE_UNIT -> size/1024/1024
        GIGABYE_UNIT -> size/1024/1024/1024
        TERABYTE_UNIT -> size/1024/1024/1024
        else -> 0
    }
    return newSize.toDouble()
}

fun createFileWithExistsName(folderPath: String,fileName :String, extension: String?) : File {
    return if(extension==null){
        File(folderPath,fileName)
    }else{
        File(folderPath,"$fileName.$extension")
    }
}

fun trimFolderCache(folderPath: String) {
    try {
        val dir = File(folderPath)
        if (dir.isDirectory) {
            deleteDir(dir)
        }
    } catch (e: Exception) {
        e.message?.let { Log.d("TRIM-CACHE", it) }
    }
}

fun deleteDir(dir: File?): Boolean {
    if (dir != null && dir.isDirectory) {
        val children = dir.list()
        if(children!=null){
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
    }

    // The directory is now empty so delete it
    return dir!!.delete()
}

/** Use external media if it is available, our app's file directory otherwise */
fun getOutputDirectory(context: Context): File {
    val appContext = context.applicationContext
    val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
        File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
    return if (mediaDir != null && mediaDir.exists())
        mediaDir else appContext.filesDir
}


fun checkFolderIsExistsIfNoteCreateOne(filePath: String, folderName: String):String{
    if (!File("$filePath/$folderName").exists()) {
        //if not exits -> create image folder
        val newFolder = File(filePath, folderName)
        newFolder.mkdirs()
        if (newFolder.exists()) {
            //get path of image directory
            return newFolder.absolutePath
        }
    }//else
    return "$filePath/$folderName"
}

//@Throws(IOException::class)
//fun copyFile(source: File?, destination: File?) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        FileUtils.copy(FileInputStream(source), FileOutputStream(destination))
//    }
//}

fun copyFileOrDirectory(srcDir: String, dstDir: String) {
    try {
        val src = File(srcDir)
        val dst = File(dstDir, src.name)
        if (src.isDirectory) {
            val files = src.list()
            files?.let {
                val filesLength = it.size
                for (i in 0 until filesLength) {
                    val src1 = File(src, it[i]).path
                    val dst1 = dst.path
                    copyFileOrDirectory(src1, dst1)
                }
            }
        } else {
            copyFile(src, dst)
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

@Throws(IOException::class)
fun copyFile(sourceFile: File?, destFile: File) {
    if (destFile.parentFile != null) {
        if(!destFile.parentFile!!.exists()){
            destFile.parentFile?.mkdirs()
        }
    }
    if (!destFile.exists()) {
        destFile.createNewFile()
    }
    var source: FileChannel? = null
    var destination: FileChannel? = null
    try {
        source = FileInputStream(sourceFile).channel
        destination = FileOutputStream(destFile).channel
        destination.transferFrom(source, 0, source.size())
    } finally {
        source?.close()
        destination?.close()
    }
}

suspend fun getFileFromInputStream(tempFile: File, inputStream: InputStream): File = suspendCoroutine { result->
    inputStream.use { input ->
        tempFile.outputStream().use { output->
            input.copyTo(output)
        }
    }
    result.resume(tempFile)
}

fun scanFileToMediaStorage(context: Context, file: File){
    val mimeType = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(file.extension)
    MediaScannerConnection.scanFile(
        context,
        arrayOf(file.absolutePath),
        arrayOf(mimeType)
    ) { _, uri ->
        Log.d(FILE_HELPER_TAG, "Image capture scanned into media store: $uri")
    }
}