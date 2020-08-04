package com.justordercompany.client.ui.custom_views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseView(val inflater: LayoutInflater, val parent: ViewGroup?)
{
    abstract fun getView(): View
}