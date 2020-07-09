package com.justordercompany.client.base

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.justordercompany.client.R
import com.justordercompany.client.di.activity.ComponentActivity
import com.justordercompany.client.di.activity.ModuleActivity
import com.justordercompany.client.di.application.ComponentApplication
import com.justordercompany.client.extensions.*
import com.justordercompany.client.logic.utils.images.ImageCameraManager
import com.justordercompany.client.logic.utils.MessagesManager
import com.justordercompany.client.logic.utils.MyVmFactory
import com.justordercompany.client.logic.utils.PermissionManager
import com.justordercompany.client.logic.utils.files.FileManager
import com.justordercompany.client.logic.utils.files.MyFileItem
import com.justordercompany.client.ui.dialogs.DialogBottomSheetRounded
import com.yalantis.ucrop.UCrop
import io.reactivex.disposables.CompositeDisposable
import java.lang.RuntimeException
import javax.inject.Inject
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition


abstract class BaseActivity : AppCompatActivity()
{
    @Inject
    lateinit var my_vm_factory: MyVmFactory

    @Inject
    lateinit var permissions_manager: PermissionManager

    @Inject
    lateinit var messages_manager: MessagesManager

    @Inject
    lateinit var image_camera_manager: ImageCameraManager

    var color_status_bar: Int = getColorMy(R.color.orange)
    var is_light_status_bar: Boolean = false
    var color_nav_bar: Int = getColorMy(R.color.white)
    var is_light_nav_bar: Boolean = false

    val composite_diposable = CompositeDisposable()

    private var base_vms: ArrayList<BaseViewModel> = arrayListOf()
    var overlays: ArrayList<View> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        applyStatusNavColors()
        super.onCreate(savedInstanceState)
    }

    fun getAppComponent(): ComponentApplication
    {
        return AppClass.app_component
    }

    fun getActivityComponent(): ComponentActivity
    {
        return getAppComponent().getActivityComponent(ModuleActivity(this))
    }

    fun setBaseVmActions(base_vm: BaseViewModel)
    {
        if (base_vms.contains(base_vm))
        {
            throw RuntimeException("**** Error base vm actions alredy setted")
        }

        base_vms.add(base_vm)
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

        base_vm.ps_to_toggle_overlay
                .mainThreaded()
                .subscribe(
                    { show_time ->
                        overlays.forEach(
                            {
                                if (show_time.first)
                                {
                                    it.animateFadeOut(show_time.second)
                                }
                                else
                                {
                                    it.animateFadeIn(show_time.second)
                                }
                            })
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_show_hide_progress
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.showProgressDialog()
                        }
                        else
                        {
                            messages_manager.dismissProgressDialog()
                        }
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_to_show_dialog
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_request_permissions
                .mainThreaded()
                .subscribe(
                    {
                        it.build(this)
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_to_finish
                .mainThreaded()
                .subscribe(
                    {
                        if (it.value != null)
                        {
                            setResult(Activity.RESULT_OK, it.value)
                        }
                        finish()
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_enable_disable_screen_touches
                .mainThreaded()
                .subscribe(
                    {
                        if (it)
                        {
                            messages_manager.disableScreenTouches()
                        }
                        else
                        {
                            messages_manager.enableScreenTouches()
                        }
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_to_show_alerter
                .mainThreaded()
                .subscribe(
                    {

                        it.show(this)
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_to_show_bottom_dialog
                .mainThreaded()
                .subscribe(
                    {
                        val dialog = DialogBottomSheetRounded()
                        dialog.setBtns(it.btns)
                        dialog.show(this.supportFragmentManager, null)
                        dialog.isCancelable = it.cancel_on_touch_outside
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_pick_action
                .mainThreaded()
                .subscribe(
                    {
                        when (it)
                        {
                            ImageCameraManager.TypePick.CAMERA_IMAGE ->
                            {
                                image_camera_manager.pickCameraImage(it.action_success)
                            }

                            ImageCameraManager.TypePick.GALLERY_IMAGE ->
                            {
                                image_camera_manager.pickGalleryImage(it.action_success)
                            }
                        }
                    })
                .disposeBy(composite_diposable)

        base_vm.ps_ucrop_action
                .mainThreaded()
                .subscribe(
                    {
                        it.startLambda(this)

                    })
                .disposeBy(composite_diposable)

        base_vm.ps_act_slider
                .mainThreaded()
                .subscribe(
                    {
                        Slidr.attach(this,it)
                    }).disposeBy(composite_diposable)

        base_vm.viewAttached()
    }

    fun addBackClick(view: View, base_vm: BaseViewModel)
    {
        view.setOnClickListener(
            {
                base_vm.clickedBack()
            })
    }

    fun applyStatusNavColors()
    {
        this.setStatusBarColor(color_status_bar)
        this.setStatusLightDark(is_light_status_bar)
        this.setNavBarColor(color_nav_bar)
        this.setNavBarLightDark(is_light_nav_bar)
    }

    override fun onDestroy()
    {
        base_vms.clear()
        composite_diposable.dispose()
        super.onDestroy()
    }

    fun getFirebaseToken(action: (String) -> Unit)
    {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(this,
            { instanceIdResult ->
                val token = instanceIdResult.token
                Log.e("BaseActivity", "getFirebaseToken: Retrieved and got token $token")
                action(token)
            })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP)
        {
            Log.e("BaseActivity", "onActivityResult: Result ok!!")
        }
        else if (resultCode == UCrop.RESULT_ERROR)
        {
            Log.e("BaseActivity", "onActivityResult: Result errrroro!")
        }
    }

    fun applySliderBottomWithUpadte()
    {
        val config = SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1.0f)
                .distanceThreshold(0.3f)
                .edge(true)
                .edgeSize(0.3f)
                .scrimStartAlpha(0.0f)
                .scrimEndAlpha(0.0f)
                .build()

        Slidr.attach(this,config)
    }
}