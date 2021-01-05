/*
 * Created by Adhitya Bagas on 31/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.utils.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.helper.MappingHelper
import com.adhityabagasmiwa.dicodingsubmissionbfaa.data.model.UserGithub
import com.bumptech.glide.Glide
import java.util.concurrent.ExecutionException

internal class StackRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {

    private var cursor: Cursor? = null
    private val mWidgetItems = ArrayList<UserGithub>()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        if (cursor != null) {
            cursor?.close()
        }

        val identifyToken = Binder.clearCallingIdentity()

        cursor = context.contentResolver.query(CONTENT_URI, null, null, null, null)
        val userList = MappingHelper.mapCursorToArrayList(cursor)
        mWidgetItems.addAll(userList)

        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remote = RemoteViews(context.packageName, R.layout.item_widget)
        try {
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .fitCenter()
                .load(mWidgetItems[position].avatarUrl)
                .submit(256, 256)
                .get()
            remote.setImageViewBitmap(R.id.imgView, bitmap)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }

        val extra = bundleOf(StackWidget.EXTRA_ITEM to position)
        val fillIntent = Intent()
        fillIntent.putExtras(extra)

        remote.setOnClickFillInIntent(R.id.imgView, fillIntent)
        return remote
    }

    override fun onDestroy() {}

    override fun getCount(): Int =
        if (mWidgetItems.isEmpty()) 0 else mWidgetItems.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}