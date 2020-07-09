package com.justordercompany.client.logic.utils.builders

import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.logic.utils.files.FileManager
import com.justordercompany.client.logic.utils.files.MyFileItem
import com.yalantis.ucrop.UCrop
import java.lang.RuntimeException

class BuilderCropMy
{
    var file_original: MyFileItem? = null
    var file_copy: MyFileItem? = null
    var action_success: ((MyFileItem) -> Unit)? = null
    var action_error: (() -> Unit)? = null

    fun setFileOriginal(my_file_item: MyFileItem) = apply(
        {
            this.file_original = my_file_item
        })

    fun setFileCopy(my_file_item: MyFileItem) = apply(
        {
            this.file_copy = my_file_item
        })

    fun setActionSuccess(action: (MyFileItem) -> Unit) = apply(
        {
            this.action_success = action
        })

    fun setActionError(action: () -> Unit) = apply(
        {
            this.action_error = action_error
        })

    fun prepareUris():Pair<Uri,Uri>
    {
        if (file_original == null)
        {
            throw RuntimeException("**** Error original file should be passed ****")
        }

        if (file_copy == null)
        {
            file_copy = MyFileItem.createFromFile(FileManager.create_temp_image_file())
        }

        val uri_1 = Uri.fromFile(file_original!!.getFile())
        val uri_2 = Uri.fromFile(file_copy!!.getFile())

        return Pair(uri_1,uri_2)
    }

    fun startSimple(activity: AppCompatActivity)
    {
        val uris = prepareUris()

        UCrop.of(uris.first, uris.second)
                .withAspectRatio(1f, 1f)
                .start(activity)
    }

    fun startLambda(activity: AppCompatActivity)
    {
        val uris = prepareUris()

        val intent = UCrop.of(uris.first, uris.second)
                .withAspectRatio(1f, 1f)
                .getIntent(activity)


        activity.startForResult(intent)
        { result ->

            Log.e("BuilderCropMy", "start: got reuls")
            val action: () -> Unit =
                    {
                        action_success?.invoke(file_copy!!)
                    }

            if (file_copy!!.getFile().length() < 1000)
            {
                runActionWithDelay(2000, action)
            }
            else
            {
                action()
            }
        }.onFailed(
            { result ->

                Log.e("BuilderCropMy", "start: Error")
                action_error?.invoke()
            })
    }
}