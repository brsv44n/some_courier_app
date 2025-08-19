package com.brsv44n.some_courier.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class GpsStateReceiver(
    private val onGpsStateChanged: () -> Unit = {}
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            onGpsStateChanged()
        }
    }
}
