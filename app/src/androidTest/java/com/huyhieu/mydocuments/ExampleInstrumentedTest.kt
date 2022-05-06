package com.huyhieu.mydocuments

import android.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.huyhieu.mydocuments", appContext.packageName)
    }

    @Test
    fun testTakeScreenshot() {
        Screengrab.screenshot("before_button_click")

        // Your custom onView...
        onView(withId(com.huyhieu.mydocuments.R.id.floatingActionButton)).perform(click())
        Screengrab.screenshot("after_button_click")
    }
}