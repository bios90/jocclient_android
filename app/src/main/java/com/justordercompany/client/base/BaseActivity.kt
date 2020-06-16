package com.justordercompany.client.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.justordercompany.client.R
import com.justordercompany.client.di.activity.ComponentActivity
import com.justordercompany.client.di.activity.ModuleActivity
import com.justordercompany.client.di.application.ComponentApplication
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.utils.MessagesManager
import com.justordercompany.client.logic.view_models.MyVmFactory
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import javax.inject.Inject

abstract class BaseActivity:AppCompatActivity()
{
    @Inject
    lateinit var my_vm_factory:MyVmFactory

    @Inject
    lateinit var messages_manager:MessagesManager

    var color_status_bar: Int = getColorMy(R.color.orange)
    var is_light_status_bar: Boolean = false
    var color_nav_bar: Int = getColorMy(R.color.white)
    var is_light_nav_bar: Boolean = false

    val composite_diposable = CompositeDisposable()

    private lateinit var base_vm: BaseViewModel
    private var base_vm_actions_setted = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        applyStatusNavColors()
        super.onCreate(savedInstanceState)
    }

    fun getAppComponent():ComponentApplication
    {
        return AppClass.app_component
    }

    fun getActivityComponent():ComponentActivity
    {
        return getAppComponent().getActivityComponent(ModuleActivity(this))
    }

    fun setBaseVmActions(base_vm:BaseViewModel)
    {
        if(base_vm_actions_setted)
        {
            throw RuntimeException("**** Error base vm actions alredy setted")
        }

        this.base_vm = base_vm

        base_vm.ps_intent_builded
                .mainThreaded()
                .subscribe(
                    {
                        it.startActivity(this)
                    },
                    {
                        it.printStackTrace()
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_show_hide_progress
                .mainThreaded()
                .subscribe(
                    {
                        if(it)
                        {
                            messages_manager.showProgressDialog()
                        }
                        else
                        {
                            messages_manager.dismissProgressDialog()
                        }
                    })
                .disposeBy(composite_diposable)
    }

    fun applyStatusNavColors()
    {
        this.setStatusBarColor(color_status_bar)
        this.setStatusLightDark(is_light_status_bar)
        this.setNavBarColor(color_nav_bar)
        this.setNavBarLightDark(is_light_nav_bar)
    }
}