package com.sun.unsplash_01.utils.imagedownload

import android.app.DownloadManager
import android.app.ProgressDialog
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.sun.unsplash_01.R
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class DownloadImage(private val context: Context) {

    fun downloadImage(url: String) {
        val namePhoto = "$TITLE_PHOTO-${System.currentTimeMillis()}"
        val manager = context.getSystemService(Service.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri: Uri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
            setAllowedOverRoaming(false)
            setTitle(namePhoto)
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                namePhoto + FILE_EXTENSION
            )
        }
        manager.enqueue(request)
    }

    fun downloadImage(bitmapImage: Bitmap) {
        Toast.makeText(context, saveImage(bitmapImage), Toast.LENGTH_SHORT).show()
    }

    private fun saveImage(bitmapImage: Bitmap): String {
        val progressLoad = ProgressDialog(context).apply {
            setMessage(context.getString(R.string.text_loading))
            setCancelable(false)
            show()
        }
        try {
            val myDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            )
            if (!myDir.exists()) {
                myDir.mkdirs()
            }
            val fileName = File(myDir, "${System.currentTimeMillis()}$FILE_EXTENSION")
            val outputStream = FileOutputStream(fileName)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.run {
                flush()
                close()
            }
            progressLoad.dismiss()
            return context.getString(R.string.text_download_success)
        } catch (exception: Exception) {
            progressLoad.dismiss()
            return exception.message.toString()
        }
    }

    companion object {

        const val TITLE_PHOTO = "Unsplash"
        const val FILE_EXTENSION = ".png"

        fun newInstance(context: Context) = DownloadImage(context)
    }
}
