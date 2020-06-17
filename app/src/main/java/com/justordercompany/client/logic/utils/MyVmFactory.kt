package com.justordercompany.client.logic.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class MyVmFactory(private val activity: AppCompatActivity)
{
    fun <T : ViewModel> createFactory(): ViewModelProvider.Factory
    {
        val factory = object : ViewModelProvider.Factory
        {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T
            {
                return modelClass.newInstance()
            }
        }

        return factory
    }

    fun <T : ViewModel> getViewModel(vm_class: Class<T>): T
    {
        val factory = createFactory<T>()
        return ViewModelProvider(activity, factory).get(vm_class)
    }
}