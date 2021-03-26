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
import android.content.pm.ActivityInfo
import android.graphics.Color
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import android.os.Build
import android.view.WindowManager
import com.justordercompany.client.logic.models.ObjWithImageUrl
import com.justordercompany.client.logic.utils.images.GlideManager
import com.stfalcon.imageviewer.StfalconImageViewer
import io.reactivex.subjects.PublishSubject


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
    var is_full_screen: Boolean = false
    var is_below_nav_bar: Boolean = false

    val composite_diposable = CompositeDisposable()

    private var base_vms: ArrayList<BaseViewModel> = arrayListOf()
    var overlays: ArrayList<View> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        applyStatusNavColors()
        makePortrait()
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        AppClass.top_activity = this
    }

    override fun onPause()
    {
        clearTopActivity()
        super.onPause()
    }

    private fun clearTopActivity()
    {
        val current_top = AppClass.top_activity
        if (this.equals(current_top))
        {
            AppClass.top_activity = null
        }
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
                        Slidr.attach(this, it)
                    }).disposeBy(composite_diposable)

        base_vm.ps_to_show_images_slider
                .mainThreaded()
                .subscribe(
                    {
                        showImagesCarousel(it)
                    })
                .disposeBy(composite_diposable)

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
        if (is_full_screen)
        {
            makeFullScreen(is_below_nav_bar)
        }

        this.setStatusBarColor(color_status_bar)
        this.setNavBarColor(color_nav_bar)
        this.setStatusLightDark(is_light_status_bar)
        this.setNavBarLightDark(is_light_nav_bar)
    }

    override fun onDestroy()
    {
        base_vms.clear()
        composite_diposable.dispose()
        clearTopActivity()
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

    fun applySliderBottom(edge_size:Float = 0.3f)
    {
        val config = SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1.0f)
                .distanceThreshold(0.3f)
                .edge(true)
                .edgeSize(edge_size)
                .scrimStartAlpha(0.5f)
                .scrimEndAlpha(0.0f)
                .build()

        Slidr.attach(this, config)
    }

    protected fun makePortrait()
    {
        try
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        catch (e: Exception)
        {

        }
    }

    protected fun makeLanscape()
    {
        try
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        catch (e: Exception)
        {

        }
    }

    protected fun makeOrintationChangable()
    {
        try
        {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
        }
        catch (e: Exception)
        {

        }
    }

    private fun showImagesCarousel(data: Pair<ArrayList<out ObjWithImageUrl>, Int?>)
    {
        val image_strs = data.first.map({ it.image_url }).filterNotNull()

        Log.e("BaseActivity", "showImagesCarousel: in size is ${data.first.size} and filtered size is ${image_strs.size}")

        val pos = data.second ?: 0
        val viewer = StfalconImageViewer.Builder<String>(this, image_strs,
            { view, url ->
                GlideManager.loadImageSimple(url, view)
            })
                .withHiddenStatusBar(false)
                .allowSwipeToDismiss(true)
                .withStartPosition(pos)
                .show()
    }
}