package com.brsv44n.some_courier.core.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EventUnitTest {

    @Test
    fun event_GetContent_MarksAsHandled() {
        val event1 = Event(1)
        event1.getContentIfNotHandled()
        assertTrue(event1.hasBeenHandled)
        assertTrue(event1.getContentIfNotHandled() == null)
    }

    @Test
    fun event_Peek_DoesNotMarksAsHandled() {
        val event1 = Event(1)
        event1.peekContent()
        assertFalse(event1.hasBeenHandled)
        assertTrue(event1.getContentIfNotHandled() == 1)
    }
}