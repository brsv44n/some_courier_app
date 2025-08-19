package com.brsv44n.some_courier

import android.app.Application
import com.brsv44n.some_courier.di.components.AppDiComponent
import com.brsv44n.some_courier.di.components.DecomposeDiComponent
import com.brsv44n.some_courier.di.components.create
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.timetravel.store.TimeTravelStoreFactory
import ru.rustore.sdk.pushclient.RuStorePushClient
import ru.rustore.sdk.pushclient.common.logger.DefaultLogger
import timber.log.Timber

class App : Application() {

    val appDiComponent by lazy {
        AppDiComponent::class.create(
            application = this,
            storeFactory = LoggingStoreFactory(TimeTravelStoreFactory()),
            context = this
        )
    }

    val decomposeDiComponent by lazy {
        DecomposeDiComponent::class.create(appDiComponent)
    }

    override fun onCreate() {
        super.onCreate()
        initRuStore()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initRuStore() {
        RuStorePushClient.init(
            application = this,
            projectId = BuildConfig.RUSTORE_PROJECT_ID,
            logger = DefaultLogger("RuStore")
        )
    }
}
