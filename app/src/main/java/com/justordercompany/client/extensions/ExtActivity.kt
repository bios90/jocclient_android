package com.justordercompany.client.extensions

import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.setStatusBarColor(color: Int)
{
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.setStatusBarColor(color)
}

fun AppCompatActivity.setStatusLightDark(is_light: Boolean)
{
    if (Build.VERSION.SDK_INT < 23)
    {
        return
    }

    var flags = window.decorView.systemUiVisibility
    if (is_light)
    {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
    else
    {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    window.decorView.systemUiVisibility = flags
}

fun AppCompatActivity.setNavBarColor(color: Int)
{
    window.setNavigationBarColor(color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
    {
        window.setNavigationBarDividerColor(color)
    }
}

fun AppCompatActivity.setNavBarLightDark(is_light: Boolean)
{
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
    {
        return
    }

    var flags = window.decorView.systemUiVisibility
    if (is_light)
    {
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
    else
    {
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    window.decorView.systemUiVisibility = flags
}