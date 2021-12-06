package ar.com.wolox.android.cookbook.notifications

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ar.com.wolox.android.cookbook.R
import ar.com.wolox.android.cookbook.databinding.FragmentNotificationBinding
import ar.com.wolox.android.cookbook.facebooklogin.FacebookLoginRecipeActivity
import ar.com.wolox.android.cookbook.googlelogin.GoogleLoginRecipeActivity
import ar.com.wolox.android.cookbook.notifications.helper.NotificationFactory
import ar.com.wolox.android.cookbook.notifications.model.BasicNotification
import ar.com.wolox.android.cookbook.notifications.model.InboxNotification
import ar.com.wolox.android.cookbook.notifications.model.NotificationAction
import ar.com.wolox.android.cookbook.notifications.model.NotificationType
import ar.com.wolox.android.cookbook.notifications.model.PictureExpandableNotification
import ar.com.wolox.android.cookbook.notifications.model.TextExpandableNotification
import ar.com.wolox.wolmo.core.fragment.WolmoFragment
import ar.com.wolox.wolmo.core.presenter.BasePresenter
import javax.inject.Inject

class NotificationFragment : WolmoFragment<FragmentNotificationBinding, BasePresenter<Any>>() {

    private lateinit var generalChannelId: String

    @Inject
    lateinit var notificationFactory: NotificationFactory

    override fun layout(): Int = R.layout.fragment_notification

    override fun init() {
        generalChannelId = getString(R.string.notifications_general_channel_id)

        binding!!.vSettingsChannel.visibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) View.VISIBLE else View.GONE
    }

    override fun setListeners() {
        with(binding!!) {
            vSimpleNotificationBtn.setOnClickListener { showBasicNotification() }
            vExpandableTextNotificationBtn.setOnClickListener { showTextExpandableNotification() }
            vBigPictureNotificationBtn.setOnClickListener { showPictureNotification() }
            vActionsNotificationBtn.setOnClickListener { showBasicNotificationWithActions() }
            vInboxNotificationBtn.setOnClickListener { showInboxNotification() }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vSettingsChannel.setOnClickListener { openChannelSettings() }
            }
        }
    }

    private fun showBasicNotification() {
        notificationFactory.showNotification(NotificationType.BASIC.ordinal, BasicNotification(
            generalChannelId,
            getString(R.string.notifications_basic_title),
            getString(R.string.notifications_short_content),
            NotificationCompat.PRIORITY_MAX
        ))
    }

    private fun showTextExpandableNotification() {
        notificationFactory.showNotification(NotificationType.TEXT_EXPANDABLE.ordinal, TextExpandableNotification(
            generalChannelId,
            getString(R.string.notifications_basic_title),
            getString(R.string.notifications_short_content),
            NotificationCompat.PRIORITY_DEFAULT,
            getString(R.string.notifications_large_content)
        ))
    }

    private fun showPictureNotification() {
        notificationFactory.showNotification(NotificationType.PICTURE_EXPANDABLE.ordinal, PictureExpandableNotification(
            generalChannelId,
            getString(R.string.notifications_basic_title),
            getString(R.string.notifications_short_content),
            NotificationCompat.PRIORITY_DEFAULT,
            getString(R.string.notifications_large_content),
            R.drawable.bg_cool_cat
        ))
    }

    private fun showBasicNotificationWithActions() {
        val fbLoginIntent = PendingIntent.getActivity(requireContext(), 0, Intent(requireContext(), FacebookLoginRecipeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
        val gLoginIntent = PendingIntent.getActivity(requireContext(), 0, Intent(requireContext(), GoogleLoginRecipeActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        notificationFactory.showNotification(NotificationType.BASIC_ACTION.ordinal, BasicNotification(
            generalChannelId,
            getString(R.string.notifications_basic_title),
            getString(R.string.notifications_short_content),
            NotificationCompat.PRIORITY_DEFAULT,
            listOf(
                NotificationAction(android.R.drawable.ic_menu_send, getString(R.string.recipe_picker_facebook_login), fbLoginIntent),
                NotificationAction(android.R.drawable.ic_dialog_email, getString(R.string.recipe_picker_google_login), gLoginIntent)
            ))
        )
    }

    private fun showInboxNotification() {
        notificationFactory.showNotification(NotificationType.INBOX.ordinal, InboxNotification(
            generalChannelId,
            getString(R.string.notifications_basic_title),
            getString(R.string.notifications_short_content),
            NotificationCompat.PRIORITY_HIGH,
            listOf(getString(R.string.notifications_inbox_line_1), getString(R.string.notifications_inbox_line_2))
        ))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openChannelSettings() {
        Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, getString(R.string.notifications_general_channel_id))
            startActivity(this)
        }
    }
}