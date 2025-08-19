package com.brsv44n.some_courier.core.di.annotations

import me.tatarka.inject.annotations.Scope
import kotlin.annotation.AnnotationTarget.*

@Scope
@Target(CLASS, FUNCTION, PROPERTY_GETTER, PROPERTY)
annotation class Singleton
