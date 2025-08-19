package com.brsv44n.some_courier.location.android

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import com.brsv44n.some_courier.R
import com.brsv44n.some_courier.location.ResolvableException

class GpsDisabledResolvableApiException : ResolvableException() {
    override fun resolve(activity: Activity, requestCode: Int) {
        AlertDialog.Builder(activity)
            .setTitle(activity.getString(R.string.title_gps_disabled))
            .setMessage(activity.getString(R.string.label_gps_disabled))
            .setNegativeButton(activity.getString(R.string.action_cancel)) { _, _ -> }
            .setPositiveButton(activity.getString(R.string.action_settings)) { _, _ ->
                activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .show()
    }
}
