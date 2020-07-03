package com.justordercompany.client.logic.utils.files

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import com.justordercompany.client.base.AppClass
import com.justordercompany.client.base.Constants
import com.justordercompany.client.logic.utils.strings.StringManager
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.net.URLConnection

class FileManager
{
    companion object
    {
        fun create_temp_image_file(extension: String? = Constants.EXTENSION_PNG): File
        {
            val name = StringManager.getNameForNewFile(extension!!)
            return createFile(name, null, Constants.FOLDER_TEMP_FILES)
        }

        fun createFile(name: String, extansion: String?, folder: String): File
        {
            val file: File

            val folder_file = File(getRootFolder() + "/" + folder)
            if (!folder_file.exists())
            {
                folder_file.mkdirs()
            }

            var file_name = name
            if (extansion != null && extansion.length > 0)
            {
                file_name = file_name + "." + extansion
            }

            Log.e("FileManager", "createFile: Will create new file $file_name in folder $folder");

            file = File(folder_file, file_name)
            if (file.exists())
            {
                file.delete()
            }

            file.createNewFile()

            return file
        }

        fun getRootFolder(): String
        {
            return Environment.getExternalStorageDirectory()!!.toString()
        }

        fun uriFromFile(file: File): Uri
        {
            var uri: Uri? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                uri = FileProvider.getUriForFile(
                    AppClass.app,
                    AppClass.app.packageName + ".provider",
                    file
                )
            }
            else
            {
                uri = Uri.fromFile(file)
            }

            if (uri == null)
            {
                throw RuntimeException("Error could not create uri!")
            }

            return uri
        }

        fun isContentImage(uri: Uri): Boolean
        {
            val cR = AppClass.app.getContentResolver()
            val type = cR.getType(uri)
            return type != null && type!!.startsWith("image")
        }

        fun isContentVideo(uri: Uri): Boolean
        {
            val cR = AppClass.app.getContentResolver()
            val type = cR.getType(uri)
            return type != null && type!!.startsWith("video")
        }

        fun getExtensionFromContentUri(uri: Uri): String?
        {
            val extension: String?

            if (uri.scheme == ContentResolver.SCHEME_CONTENT)
            {
                val mime = MimeTypeMap.getSingleton()
                extension = mime.getExtensionFromMimeType(AppClass.app.getContentResolver().getType(uri))
            }
            else
            {
                extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
            }

            return extension
        }

        fun copyToFileFromIntentData(file: File, data: Uri)
        {
            val inputStream = AppClass.app.contentResolver.openInputStream(data)
            val outputStream = FileOutputStream(file)

            copy(inputStream!!, outputStream)
        }

        fun copy(input: InputStream, output: OutputStream)
        {
            try
            {
                val buf: ByteArray = ByteArray(1024)
                var len: Int = input.read(buf)

                while (len > 0)
                {
                    output.write(buf, 0, len)
                    len = input.read(buf)
                }
            }
            catch (e: java.lang.Exception)
            {
            }
            finally
            {
                if (input != null)
                {
                    try
                    {
                        input.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }

                }

                if (output != null)
                {
                    try
                    {
                        output.close()
                    }
                    catch (ioex: IOException)
                    {
                        Log.e("FileManager", "Exception : " + ioex.message);
                    }
                }
            }
        }

        fun isFileImage(path_real: String): Boolean
        {
            val mimeType = URLConnection.guessContentTypeFromName(path_real)
            return mimeType != null && mimeType!!.startsWith("image")
        }
    }
}


fun File.toPartBody(field_name: String): MultipartBody.Part?
{
    var bodyFile: MultipartBody.Part? = null

    if (this.exists())
    {
        val requestBody = RequestBody.create(MediaType.parse("multipart/form-baby_diary"), this)
        bodyFile = MultipartBody.Part.createFormData(field_name, this.getName(), requestBody)
    }

    return bodyFile
}