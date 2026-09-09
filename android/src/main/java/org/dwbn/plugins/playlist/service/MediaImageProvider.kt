package org.dwbn.plugins.playlist.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.devbrackets.android.playlistcore.components.image.ImageProvider
import org.dwbn.plugins.playlist.FakeR
import org.dwbn.plugins.playlist.data.AudioTrack
import org.dwbn.plugins.playlist.manager.Options


class MediaImageProvider(
    context: Context,
    val onImageUpdatedListener: OnImageUpdatedListener,
    options: Options
) : ImageProvider<AudioTrack> {

    interface OnImageUpdatedListener {
        fun onImageUpdated()
    }

    private var options: Options? = null
    private val glide: RequestManager = Glide.with(context.applicationContext)
    private val fakeR: FakeR = FakeR(context.applicationContext)
    private val remoteViewImageTarget = RemoteViewImageTarget()
    private var defaultArtworkImage: Bitmap? = null
    private var artworkImage: Bitmap? = null
    private var notificationIconId = 0
    override val notificationIconRes: Int
        get() = mipmapIcon

    override val remoteViewIconRes: Int
        get() = mipmapIcon

    override val largeNotificationImage: Bitmap?
        get() = remoteViewArtwork

    override var remoteViewArtwork: Bitmap? = null
        get() = if (artworkImage != null) artworkImage else defaultArtworkImage
        private set

    override fun updateImages(playlistItem: AudioTrack) {
        glide.asBitmap().load(playlistItem.artworkUrl).into(remoteViewImageTarget)
    }

    // return R.mipmap.icon; // this comes from cordova itself.
    private val mipmapIcon: Int
        get() {
            // return R.mipmap.icon; // this comes from cordova itself.
            if (notificationIconId <= 0) {
                notificationIconId = fakeR.getId("drawable", options?.icon)
            }
            if (notificationIconId <= 0) {
                // Posting a notification with small icon 0 throws
                // "Invalid notification (no valid small icon)" and kills the
                // host app on the first play. A host can lose the drawable
                // without noticing (e.g. shrinkResources strips a resource
                // that is only referenced by name), so fall back to the
                // launcher icon instead of crashing.
                notificationIconId = fakeR.context.applicationInfo.icon
            }
            return notificationIconId
        }

    /**
     * A class used to listen to the loading of the large lock screen images and perform
     * the correct functionality to update the artwork once it is loaded.
     *
     * **NOTE:** This is a Glide Image loader class
     */
    private inner class RemoteViewImageTarget : CustomTarget<Bitmap>() {
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            artworkImage = resource
        }

        override fun onLoadCleared(placeholder: android.graphics.drawable.Drawable?) {
            // No cleanup needed
        }
    }

    init {
        this.options = options
        defaultArtworkImage = BitmapFactory.decodeResource(
            context.resources,
            fakeR.getId("drawable", options.icon)
        )
    }
}