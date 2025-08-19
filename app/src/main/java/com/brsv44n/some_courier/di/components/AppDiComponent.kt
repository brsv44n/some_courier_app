package com.brsv44n.some_courier.di.components

import android.app.Application
import android.content.Context
import com.brsv44n.some_courier.data.ApiService
import com.brsv44n.some_courier.data.PermissionsObserver
import com.brsv44n.some_courier.data.ProfileDataRepository
import com.brsv44n.some_courier.domain.repository.ProfileRepository
import com.brsv44n.some_courier.domain.session.SessionClearable
import com.brsv44n.some_courier.location.LocationServicesFactoryProvider
import com.brsv44n.some_courier.messaging.ChpMessagingTokenProvider
import com.brsv44n.some_courier.messaging.core.ChpPushNotificationIntentBuilder
import com.brsv44n.some_courier.messaging.core.MessageActionParser
import com.brsv44n.some_courier.messaging.core.MessagingTokenProvider
import com.brsv44n.some_courier.messaging.core.NotificationChannelCreator
import com.brsv44n.some_courier.messaging.core.PushMessageNotifier
import com.brsv44n.some_courier.messaging.core.PushNotificationIntentBuilder
import com.brsv44n.some_courier.messaging.rustore.MessageContentExtractor
import com.brsv44n.some_courier.location.LocationNotificationBuilder
import com.brsv44n.some_courier.location.LocationUtils
import com.brsv44n.some_courier.utils.AndroidExternalAppNavigator
import com.brsv44n.some_courier.core.android_utils.AndroidResourceManager
import com.brsv44n.some_courier.core.di.annotations.Singleton
import com.brsv44n.some_courier.core.presentation.mapper.DateTimeFormatter
import com.brsv44n.some_courier.core.popup.MessageNotifier
import com.brsv44n.some_courier.core.popup.MessageNotifierImpl
import com.brsv44n.some_courier.core.utils.ExternalAppNavigator
import com.brsv44n.some_courier.core.utils.ResourceManager
import com.arkivanov.mvikotlin.core.store.StoreFactory
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides

@Singleton
@Component
abstract class AppDiComponent(
    @get:Provides override val application: Application,
    @get:Provides override val context: Context,
    @get:Provides val storeFactory: StoreFactory,
) : NetworkDiComponent(application, context) {

    abstract val locationUtils: LocationUtils
    abstract val locationNotificationBuilder: LocationNotificationBuilder
    abstract val locationServicesFactoryProvider: LocationServicesFactoryProvider
    abstract val permissionsObserver: PermissionsObserver
    abstract val apiService: ApiService

    abstract val messageActionParser: MessageActionParser
    abstract val messageContentExtractor: MessageContentExtractor

    @Singleton
    abstract val pushMessageNotifier: PushMessageNotifier

    @Singleton
    abstract val profileDataRepository: ProfileDataRepository

    @Singleton
    abstract val notificationChannelCreator: NotificationChannelCreator

    @Singleton
    @get:Provides
    val dateTimeFormatter: DateTimeFormatter
        get() = DateTimeFormatter(application)

    @Provides
    fun provideResourceManager(): ResourceManager = AndroidResourceManager(context)

    @Singleton
    @Provides
    fun provideMessageNotifier(): MessageNotifier = MessageNotifierImpl()

    @Singleton
    @Provides
    fun provideExternalAppNavigator(): ExternalAppNavigator =
        AndroidExternalAppNavigator(application)

    @Singleton
    @Provides
    fun provideProfileRepository(): ProfileRepository = profileDataRepository

    @Provides
    @IntoSet
    fun provideProfileRepositoryClearable(): SessionClearable = profileDataRepository

    @Provides
    @IntoSet
    fun provideAuthHolderClearable(): SessionClearable = androidAuthHolder

    @Singleton
    @Provides
    fun ChpPushNotificationIntentBuilder.bind(): PushNotificationIntentBuilder = this

    @Singleton
    @Provides
    fun ChpMessagingTokenProvider.bind(): MessagingTokenProvider = this
}
