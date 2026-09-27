package com.ticketcompare.movies

import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.engine.ClientPriceCalculationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PriceCalculationEngineTest {

    @Test
    fun testBasePriceSeatMultiplier() {
        val classicBreakdown = ClientPriceCalculationEngine.calculate(
            platformId = "bms",
            platformName = "BookMyShow",
            baseTicketPrice = 200,
            ticketCount = 2,
            seatCategory = "Classic"
        )
        // 200 * 1.0 * 2 = 400
        assertEquals(400, classicBreakdown.basePrice)

        val reclinerBreakdown = ClientPriceCalculationEngine.calculate(
            platformId = "bms",
            platformName = "BookMyShow",
            baseTicketPrice = 200,
            ticketCount = 2,
            seatCategory = "Recliner"
        )
        // 200 * 1.75 = 350 * 2 = 700
        assertEquals(700, reclinerBreakdown.basePrice)
    }

    @Test
    fun testPlatformFeesAndGstCalculation() {
        val breakdown = ClientPriceCalculationEngine.calculate(
            platformId = "district",
            platformName = "District",
            baseTicketPrice = 250,
            ticketCount = 2,
            seatCategory = "Classic"
        )
        // District fee = 18 * 2 = 36
        // Internet handling = 5 * 2 = 10
        // Combined fee = 46
        // 18% GST on 46 = 8.28 -> 8
        assertEquals(36, breakdown.convenienceFee)
        assertEquals(10, breakdown.internetHandlingFee)
        assertEquals(8, breakdown.gstOnFees)
        assertEquals(500 + 36 + 10 + 8, breakdown.grossAmount)
    }

    @Test
    fun testHdfcCreditCardInstantDiscountReducesPayable() {
        val hdfcCard = SavedPaymentMethod(
            id = "1",
            category = "CREDIT_CARD",
            bank = "HDFC",
            cardType = "CREDIT",
            isSelected = true
        )

        val breakdown = ClientPriceCalculationEngine.calculate(
            platformId = "bms",
            platformName = "BookMyShow",
            baseTicketPrice = 250,
            ticketCount = 2,
            seatCategory = "Classic",
            userPaymentMethods = listOf(hdfcCard)
        )

        // Gross: 500 + 56 + 10 + 12 = 578
        // 25% of 578 = 144.5 -> 145 (max 150)
        assertEquals(145, breakdown.bankDiscount)
        assertEquals(breakdown.grossAmount - 145, breakdown.finalPayableAmount)
        assertEquals(0, breakdown.potentialCashback)
    }

    @Test
    fun testGooglePayCashbackSeparationFromFinalPayable() {
        val gpay = SavedPaymentMethod(
            id = "2",
            category = "UPI",
            providerApp = "GOOGLE_PAY",
            isSelected = true
        )

        val breakdown = ClientPriceCalculationEngine.calculate(
            platformId = "district",
            platformName = "District",
            baseTicketPrice = 250,
            ticketCount = 2,
            seatCategory = "Classic",
            userPaymentMethods = listOf(gpay)
        )

        // Final payable MUST NOT be reduced by cashback
        assertEquals(0, breakdown.upiDiscount)
        assertEquals(breakdown.grossAmount, breakdown.finalPayableAmount)

        // Potential cashback must be separated
        assertEquals(75, breakdown.potentialCashback)
        assertEquals(breakdown.finalPayableAmount - 75, breakdown.effectiveCost)
    }

    @Test
    fun testCouponApplicationSave100() {
        val breakdown = ClientPriceCalculationEngine.calculate(
            platformId = "pvr",
            platformName = "PVR INOX",
            baseTicketPrice = 250,
            ticketCount = 2,
            seatCategory = "Classic",
            enteredCouponCode = "SAVE100"
        )

        assertEquals(100, breakdown.couponDiscount)
        assertEquals(breakdown.grossAmount - 100, breakdown.finalPayableAmount)
    }
}
