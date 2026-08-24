package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.GlobalNetRepository
import com.example.model.PredefinedPackages
import com.example.model.UserProfile
import com.example.service.SmsNotificationHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("GlobalNet", appName)
    }

    @Test
    fun `validate predefined 5 packages and codes`() {
        assertEquals(5, PredefinedPackages.packages.size)

        val pkg2211 = PredefinedPackages.findByCode("2211")
        assertNotNull(pkg2211)
        assertEquals(25, pkg2211!!.generalDataGb)
        assertEquals(25, pkg2211.socialDataGb)
        assertEquals(15, pkg2211.validityDays)

        val pkg2222 = PredefinedPackages.findByCode("2222")
        assertNotNull(pkg2222)
        assertEquals(50, pkg2222!!.generalDataGb)
        assertEquals(50, pkg2222.socialDataGb)
        assertEquals(30, pkg2222.validityDays)

        val pkg2233 = PredefinedPackages.findByCode("2233")
        assertNotNull(pkg2233)
        assertEquals(100, pkg2233!!.generalDataGb)
        assertEquals(100, pkg2233.socialDataGb)
        assertEquals(40, pkg2233.validityDays)

        val pkg2244 = PredefinedPackages.findByCode("2244")
        assertNotNull(pkg2244)
        assertEquals(150, pkg2244!!.generalDataGb)
        assertEquals(150, pkg2244.socialDataGb)
        assertEquals(60, pkg2244.validityDays)

        val pkg2255 = PredefinedPackages.findByCode("2255")
        assertNotNull(pkg2255)
        assertEquals(200, pkg2255!!.generalDataGb)
        assertEquals(200, pkg2255.socialDataGb)
        assertEquals(90, pkg2255.validityDays)

        assertNull(PredefinedPackages.findByCode("9999"))
    }

    @Test
    fun `test repository package activation and persistence`() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val repo = GlobalNetRepository(context)

        val user = UserProfile(
            fullName = "John Doe",
            phoneNumber = "+1234567890",
            country = "United States",
            city = "New York",
            preciseLocation = "40.7128° N, 74.0060° W",
            isRegistered = true
        )
        repo.saveUserProfile(user)

        val retrieved = repo.getUserProfile()
        assertEquals("John Doe", retrieved.fullName)
        assertEquals("+1234567890", retrieved.phoneNumber)
        assertEquals("United States", retrieved.country)

        val pkg = PredefinedPackages.findByCode("2222")!!
        val sub = repo.activatePackage(pkg)
        assertEquals("2222", sub.packageCode)
        assertEquals(50.0, sub.remainingGeneralGb, 0.01)
        assertEquals(50.0, sub.remainingSocialGb, 0.01)
        assertTrue(sub.isConnected)

        val storedSub = repo.getActiveSubscription()
        assertNotNull(storedSub)
        assertEquals("2222", storedSub!!.packageCode)
    }

    @Test
    fun `test sms helper formatted confirmation message`() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        val smsHelper = SmsNotificationHelper(context)
        val pkg = PredefinedPackages.findByCode("2233")!!
        val confirmation = smsHelper.generateConfirmationSmsText(pkg, "Sarah")

        assertTrue(confirmation.contains("2233"))
        assertTrue(confirmation.contains("100GB General"))
        assertTrue(confirmation.contains("100GB Social"))
        assertTrue(confirmation.contains("40 Days"))
        assertTrue(confirmation.contains("100% Free"))
    }
}
