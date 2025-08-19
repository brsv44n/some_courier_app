package com.brsv44n.some_courier.core.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OptionalUnitTest {

    @Test
    fun optional_EmptyConstructor_ReturnsNull() {
        assertTrue(Optional.empty<Any>().orNull() == null)
    }

    @Test
    fun optional_SameValues_ReturnsTrue() {
        assertTrue(Optional.empty<Any>() == Optional.empty<Int>())
        assertTrue(Optional.of(1) == Optional.of(1))
        val obj1 = "Test"
        assertTrue(Optional.of(obj1) == Optional.of(obj1))
        val obj2 = "Test"
        assertTrue(Optional.of(obj1) == Optional.of(obj2))
    }

    @Test
    fun optional_DifferentValues_ReturnsFalse() {
        assertFalse(Optional.empty<Any>() == Optional.of(1))
        assertFalse(Optional.of(1) == Optional.of(2))
        val obj1 = "Test1"
        val obj2 = "Test2"
        assertFalse(Optional.of(obj1) == Optional.of(obj2))
    }

    @Test
    fun optional_OrElseGet_ReturnsProperValues() {
        assertTrue(Optional.empty<Any>().orElseGet { 1 } == Optional.empty<Any>().orElseGet { 1 })
        assertTrue(
            Optional.empty<String>().orElseGet { "Test" } == Optional.empty<String>()
            .orElseGet { "Test" }
        )
        assertTrue(Optional.of("Test").orElseGet { null } == Optional.of("Test").orElseGet { null })
        assertTrue(Optional.of(1).orElseGet { 2 } == Optional.empty<Int>().orElseGet { 1 })

        assertFalse(
            Optional.empty<Any>().orElseGet { null } == Optional.empty<Any>()
            .orElseGet { 1 }
        )
        assertFalse(Optional.of(1).orElseGet { null } == Optional.empty<Any>().orElseGet { null })
    }

    @Test
    fun optional_isPresent_ReturnsProperBoolean() {
        assertFalse(Optional.empty<Any>().isPresent)
        assertTrue(Optional.of(1).isPresent)
        assertFalse(Optional<Any>().isPresent)
        assertTrue(Optional(1).isPresent)
    }
}
