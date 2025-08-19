package com.brsv44n.some_courier.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.browser.customtabs.CustomTabsIntent
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.core.domain.entities.PointEntity
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator

class AndroidExternalAppNavigator(
    private val context: Context,
) : ExternalAppNavigator {

    override fun openDialApp(phone: String, newTask: Boolean) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
            if (newTask) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        context.startActivity(intent)
    }

    override fun openUrl(url: String, newTask: Boolean) {
        openUrlInternal(url, true)
    }

    override fun openEmail(email: String, newTask: Boolean) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.label_select_app)
            ).apply {
                if (newTask) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
        )
    }

    @Suppress("SwallowedException")
    override fun openRouteSinglePoint(point: PointEntity, newTask: Boolean) {
        val uri = "https://2gis.ru/routeSearch/to/${point.longitude},${point.latitude}/go"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        try {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.label_select_app)
                ).apply {
                    if (newTask) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            )
        } catch (e: ActivityNotFoundException) {
            // No activity found to handle the intent
        }
    }

    @Suppress("SwallowedException")
    override fun openRouteMultiplePoints(points: List<PointEntity?>, newTask: Boolean) {
        var uri = "https://2gis.ru/directions/tab/car/points/"
        if (points.isNotEmpty()) {
            points.forEach { point ->
                point?.let {
                    uri = uri.plus("|${it.longitude},${it.latitude}")
                }
            }
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        try {
            context.startActivity(
                Intent.createChooser(
                    intent,
                    context.getString(R.string.label_select_app)
                ).apply {
                    if (newTask) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            )
        } catch (e: ActivityNotFoundException) {
            // No activity found to handle the intent
        }
    }

    override fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.parse("package:${context.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun openUrlInternal(url: String, newTask: Boolean) {
        context.openUrlWithCustomTabs(url, newTask) { context.openUrlWithBrowser(url, newTask) }
    }

    @Suppress("SwallowedException")
    private fun Context.openUrlWithCustomTabs(url: String, newTask: Boolean, fallback: () -> Unit) {
        try {
            val intent = CustomTabsIntent.Builder().build()
            if (newTask) {
                intent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.launchUrl(this, Uri.parse(url))
        } catch (e: ActivityNotFoundException) {
            fallback.invoke()
        }
    }

    @Suppress("SwallowedException")
    private fun Context.openUrlWithBrowser(url: String, newTask: Boolean = true) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            if (newTask) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // No activity found to handle the intent
        }
    }
}
