package com.justordercompany.client.logic.utils

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.R
import com.justordercompany.client.extensions.mainThreaded
import com.justordercompany.client.extensions.makeTransparentBg
import com.justordercompany.client.extensions.runActionWithDelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MessagesManager(private val activity: AppCompatActivity)
{
    private val show_delay = 700L
    private var last_time_shown: Long? = null
    private var progress_show_disposable: Disposable? = null
    var progress_dialog: AlertDialog? = null


    fun showProgressDialog()
    {
        if (progress_dialog?.isShowing == true || progress_show_disposable != null)
        {
            return
        }

        progress_show_disposable = Observable.timer(show_delay, TimeUnit.MILLISECONDS)
                .mainThreaded()
                .subscribe(
                    {
                        showProgressSimple()
                    },
                    {
                        it.printStackTrace()
                    })
    }


    fun showProgressSimple()
    {
        if (progress_dialog != null && progress_dialog!!.isShowing)
        {
            return
        }

        val dialogView =
                LayoutInflater.from(activity).inflate(R.layout.la_progress, null)

        progress_dialog = AlertDialog.Builder(activity).create()
        progress_dialog!!.setCancelable(false)
        progress_dialog!!.setCanceledOnTouchOutside(false)
        progress_dialog!!.setView(dialogView)
        progress_dialog!!.makeTransparentBg()
        try
        {
            progress_dialog!!.show()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun dismissProgressDialog()
    {
        val action =
                {
                    progress_dialog?.dismiss()
                    progress_dialog = null
                    progress_show_disposable?.dispose()
                    progress_show_disposable = null
                }

        val current_time = System.currentTimeMillis()
        if (last_time_shown != null && (current_time - last_time_shown!! < show_delay))
        {
            runActionWithDelay(show_delay.toInt(), action)
        }
        else
        {
            action()
        }
    }
}