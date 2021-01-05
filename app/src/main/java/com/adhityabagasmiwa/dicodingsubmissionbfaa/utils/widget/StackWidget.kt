/*
 * Created by Adhitya Bagas on 31/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.dicodingsubmissionbfaa.utils.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri
import com.adhityabagasmiwa.dicodingsubmissionbfaa.R

class StackWidget : AppWidgetProvider() {

    companion object {
        private const val TOAST_ACTION = "com.adhityabagasmiwa.dicodingsubmissionbfaa.TOAST_ACTION"
        const val EXTRA_ITEM = "com.adhityabagasmiwa.dicodingsubmissionbfaa.EXTRA_ITEM"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: Int) {
            val mIntent = Intent(context, StackWidgetService::class.java)
            mIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds)
            mIntent.data = mIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.stack_widget)
            views.setRemoteAdapter(R.id.stackView, mIntent)
            views.setEmptyView(R.id.stackView, R.id.tvEmptyView)

            val toastIntent = Intent(context, StackWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds)
            mIntent.data = mIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stackView, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetIds, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
