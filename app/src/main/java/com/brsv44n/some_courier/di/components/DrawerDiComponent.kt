package com.brsv44n.some_courier.di.components

import com.brsv44n.some_courier.presentation.drawer.DrawerDependencies
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
abstract class DrawerDiComponent(
    @get:Provides val dependencies: DrawerDependencies
) : InstanceKeeper.Instance
