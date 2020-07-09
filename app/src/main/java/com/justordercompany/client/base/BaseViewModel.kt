package com.justordercompany.client.base

import android.content.Intent
import android.util.Log
import androidx.core.graphics.alpha
import androidx.lifecycle.ViewModel
import com.justordercompany.client.R
import com.justordercompany.client.extensions.Optional
import com.justordercompany.client.extensions.applyTransparency
import com.justordercompany.client.extensions.getColorMy
import com.justordercompany.client.extensions.getStringMy
import com.justordercompany.client.logic.utils.*
import com.justordercompany.client.logic.utils.builders.*
import com.justordercompany.client.logic.utils.files.MyFileItem
import com.justordercompany.client.logic.utils.images.ImageCameraManager
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

interface BaseViewModelInterface
{
    var location_manager: LocationManager
    var bus_main_events: BusMainEvents
    val composite_disposable: CompositeDisposable
    val ps_intent_builded: PublishSubject<BuilderIntent>
    val ps_show_hide_progress: PublishSubject<Boolean>
    val ps_enable_disable_screen_touches: PublishSubject<Boolean>
    val ps_to_show_dialog: PublishSubject<BuilderDialogMy>
    val ps_to_show_bottom_dialog: PublishSubject<BuilderDialogBottom>
    val ps_to_show_alerter: PublishSubject<BuilderAlerter>
    val ps_request_permissions: PublishSubject<BuilderPermRequest>
    val ps_to_finish: PublishSubject<Optional<Intent>>
    val ps_pick_action: PublishSubject<ImageCameraManager.TypePick>
    val ps_ucrop_action: PublishSubject<BuilderCropMy>
    val ps_to_toggle_overlay: PublishSubject<Pair<Boolean, Int>>
    val ps_act_slider:PublishSubject<SlidrConfig>

    fun clickedBack()
    {
        ps_to_finish.onNext(Optional(null))
    }

    fun viewAttached()
}

abstract class BaseViewModel : ViewModel(), BaseViewModelInterface
{
    @Inject
    override lateinit var location_manager: LocationManager

    @Inject
    override lateinit var bus_main_events: BusMainEvents

    override val composite_disposable: CompositeDisposable = CompositeDisposable()
    override val ps_intent_builded: PublishSubject<BuilderIntent> = PublishSubject.create()
    override val ps_show_hide_progress: PublishSubject<Boolean> = PublishSubject.create()
    override val ps_enable_disable_screen_touches: PublishSubject<Boolean> = PublishSubject.create()
    override val ps_to_show_dialog: PublishSubject<BuilderDialogMy> = PublishSubject.create()
    override val ps_to_show_bottom_dialog: PublishSubject<BuilderDialogBottom> = PublishSubject.create()
    override val ps_to_show_alerter: PublishSubject<BuilderAlerter> = PublishSubject.create()
    override val ps_request_permissions: PublishSubject<BuilderPermRequest> = PublishSubject.create()
    override val ps_to_finish: PublishSubject<Optional<Intent>> = PublishSubject.create()
    override val ps_pick_action: PublishSubject<ImageCameraManager.TypePick> = PublishSubject.create()
    override val ps_ucrop_action: PublishSubject<BuilderCropMy> = PublishSubject.create()
    override val ps_to_toggle_overlay: PublishSubject<Pair<Boolean, Int>> = PublishSubject.create()
    override val ps_act_slider: PublishSubject<SlidrConfig> = PublishSubject.create()

    override fun viewAttached()
    {
        Log.e("BaseViewModel", "**** viewAttached Base Implementation ****")
    }

    fun checkAndPickImage(action_picked: (MyFileItem) -> Unit)
    {
        val text_blocked = getStringMy(R.string.for_full_work_need_permissions)

        val builder = BuilderPermRequest()
                .setPermissions(PermissionManager.permissions_camera)
                .setActionBlockedNow(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedNow(text_blocked,
                            {
                                checkAndPickImage(action_picked)
                            })
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionBlockedFinally(
                    {
                        val dialog = MessagesManager.getBuilderPermissionsBlockedFinally(text_blocked)
                        ps_to_show_dialog.onNext(dialog)
                    })
                .setActionAvailable(
                    {
                        val builder = BuilderDialogBottom()
                                .addBtn(BtnAction(getStringMy(R.string.gallery),
                                    {
                                        val pick = ImageCameraManager.TypePick.GALLERY_IMAGE
                                        pick.action_success = action_picked
                                        ps_pick_action.onNext(pick)
                                    }))
                                .addBtn(BtnAction(getStringMy(R.string.camera),
                                    {
                                        val pick = ImageCameraManager.TypePick.CAMERA_IMAGE
                                        pick.action_success = action_picked
                                        ps_pick_action.onNext(pick)
                                    }))
                                .setCancelOnTouchOutside(false)

                        ps_to_show_bottom_dialog.onNext(builder)
                    })

        ps_request_permissions.onNext(builder)
    }

    fun addBottomSlider()
    {
        val config = SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .sensitivity(1.0f)
                .distanceThreshold(0.3f)
                .edge(true)
                .edgeSize(0.8f)
                .scrimStartAlpha(0.5f)
                .scrimEndAlpha(0.0f)
                .build()

        ps_act_slider.onNext(config)
    }
}