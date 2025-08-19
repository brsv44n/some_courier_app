package com.brsv44n.some_courier

import android.content.IntentFilter
import android.graphics.Color
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.brsv44n.some_courier.presentation.root.ui.RootContent
import com.brsv44n.some_courier.presentation.ui_kit.Popup
import com.brsv44n.some_courier.location.GpsStateReceiver
import com.brsv44n.some_courier.theme.CHPAndroidCourierTheme
import com.arkivanov.decompose.defaultComponentContext

class MainActivity : ComponentActivity() {

    private val appDiComponent by lazy {
        (application as App).appDiComponent
    }

    private val messageNotifier by lazy {
        (application as App).decomposeDiComponent.messageNotifier
    }

    private val gpsStateReceiver by lazy {
        GpsStateReceiver {
            val appDiComponent = (application as App).appDiComponent
            val locationUtils = appDiComponent.locationUtils
            if (locationUtils.canStartLocationTracking()) {
                locationUtils.startLocationTracking()
            } else {
                locationUtils.stopLocationTracking()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            appDiComponent.notificationChannelCreator.invoke()
        }

        val orderId = intent?.getLongExtra("orderId", -1)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        createNotificationChannels()

        val loginRootFactory = (application as App).decomposeDiComponent.rootComponentFactory
        val component =
            loginRootFactory.invoke(componentContext = defaultComponentContext(), orderId = orderId)
        setContent {
            CHPAndroidCourierTheme {
                Popup(messageNotifier)
                RootContent(
                    modifier = Modifier.fillMaxSize(),
                    component = component,
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(gpsStateReceiver, IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION))
        appDiComponent.permissionsObserver.registerGpsReceiver()
    }

    override fun onStop() {
        appDiComponent.permissionsObserver.unregisterGpsReceiver()
        unregisterReceiver(gpsStateReceiver)
        super.onStop()
    }

    private fun createNotificationChannels() {
        appDiComponent.locationNotificationBuilder.createNotificationChannelIfNeeded()
    }
}
