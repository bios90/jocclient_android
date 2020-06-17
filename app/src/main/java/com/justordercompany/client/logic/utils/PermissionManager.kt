package com.justordercompany.client.logic.utils

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.base.BaseActivity
import com.justordercompany.client.extensions.disposeBy
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable

class PermissionBlockedFinally : RuntimeException("**** Permission blocked finally ****")
class PermissionBlockedNow : RuntimeException("**** Permission blocked now ****")

class PermissionManager(val activity: AppCompatActivity)
{
    companion object
    {
        val permissions_location = listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        val permissions_camera = listOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    val rx_permissions: RxPermissions

    init
    {
        rx_permissions = RxPermissions(activity)
    }

    fun checkAndRequest(permissions: List<String>): Completable
    {
        return rx_permissions.requestEachCombined(*permissions.toTypedArray())
                .filter(
                    {
                        if (it.granted)
                        {
                            return@filter true
                        }

                        if (it.shouldShowRequestPermissionRationale)
                        {
                            throw PermissionBlockedNow()
                        }

                        throw PermissionBlockedFinally()
                    })
                .ignoreElements()
    }
}

class BuilderPermRequest()
{
    private var permissions: List<String>? = null
    private var action_available: (() -> Unit)? = null
    private var action_blocked_now: (() -> Unit)? = null
    private var action_blocked_finally: (() -> Unit)? = null

    fun setPermissions(permissions: List<String>) = apply(
        {
            this.permissions = permissions
        })

    fun setActionAvailable(action: () -> Unit) = apply(
        {
            this.action_available = action
        })

    fun setActionBlockedNow(action: () -> Unit) = apply(
        {
            this.action_blocked_now = action
        })

    fun setActionBlockedFinally(action: () -> Unit) = apply(
        {
            this.action_blocked_finally = action
        })

    fun build(base_activity: BaseActivity)
    {
        if (permissions == null)
        {
            throw RuntimeException("**** Error no permissions passed to check ****")
        }

        base_activity.permissions_manager
                .checkAndRequest(permissions!!)
                .subscribe(
                    {
                        action_available?.invoke()
                    },
                    {
                        if (it is PermissionBlockedNow)
                        {
                            action_blocked_now?.invoke()
                        }
                        else if (it is PermissionBlockedFinally)
                        {
                            action_blocked_finally?.invoke()
                        }
                    })
                .disposeBy(base_activity.composite_diposable)
    }
}