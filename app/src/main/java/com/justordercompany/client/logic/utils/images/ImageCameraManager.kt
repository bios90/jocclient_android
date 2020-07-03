package com.justordercompany.client.logic.utils.images

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.github.florent37.inlineactivityresult.kotlin.startForResult
import com.justordercompany.client.extensions.runActionWithDelay
import com.justordercompany.client.logic.utils.MessagesManager
import com.justordercompany.client.logic.utils.files.FileManager
import com.justordercompany.client.logic.utils.files.MyFileItem
import com.yalantis.ucrop.UCrop
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ImageCameraManager(val activity: AppCompatActivity, val messagesManager: MessagesManager)
{
    enum class TypePick(var action_success: ((MyFileItem) -> Unit))
    {
        GALLERY_IMAGE({}),
        CAMERA_IMAGE({}),
    }

    fun pickCameraImage(action_success: (MyFileItem) -> Unit)
    {
        val file = FileManager.create_temp_image_file()
        val uri = FileManager.uriFromFile(file)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        activity.startForResult(intent)
        { result ->

            val action =
                    {
                        val my_file_item = MyFileItem.createFromFile(file)
                        action_success(my_file_item)
                    }
            if (file.length() < 1000)
            {
                runActionWithDelay(2000, action)
            }
            else
            {
                action()
            }

        }.onFailed(
            { result ->

            })
    }

    fun pickGalleryImage(action_success: (MyFileItem) -> Unit)
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        activity.startForResult(intent)
        { result ->

            val data = result.data?.data ?: return@startForResult

            Observable.defer({
                Observable.just(MyFileItem.createFromData(data))
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe({ messagesManager.showProgressDialog() })
                    .doFinally({ messagesManager.dismissProgressDialog() })
                    .subscribe(
                        {
                            //                            action_success(it)

                            val file_copy = MyFileItem.createFromFile(FileManager.create_temp_image_file())

                            val uri_1 = Uri.fromFile(it.getFile())
                            val uri_2 = Uri.fromFile(file_copy.getFile())
                            UCrop.of(uri_1, uri_2)
                                    .withAspectRatio(1f, 1f)
//                                    .getIntent(activity)
                                    .start(activity)

                        },
                        {
                            it.printStackTrace()
                        })

        }.onFailed(
            { result ->

            })
    }
}