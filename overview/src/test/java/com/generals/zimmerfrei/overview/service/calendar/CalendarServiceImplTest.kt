package com.generals.zimmerfrei.overview.service.calendar

import com.generals.zimmerfrei.overview.model.Day
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.junit.Test

class CalendarServiceImplTest {

    private val sut: CalendarService = CalendarServiceImpl()

    @Test
    fun shouldLoadCalendar() {

        val testObserver: TestObserver<List<Day>> = sut.loadCalendar()
            .test()

        assertEquals(1, testObserver.events[0].size)
        assertEquals(1, testObserver.events[2].size)

        val firstEvent: List<Day> = testObserver.events[0][0] as List<Day>

        assertEquals("1 DOM", firstEvent[0].title)
    }
}