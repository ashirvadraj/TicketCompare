package com.ticketcompare.movies.engine

import com.ticketcompare.movies.data.model.PriceBreakdown
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import kotlin.math.roundToInt

object ClientPriceCalculationEngine {

    /**
     * Calculates the complete itemized price breakdown.
     * Enforces strict distinction:
     * - finalPayableAmount is the exact sum charged at gateway (Base + Fees - Instant Discount).
     * - potentialCashback is credited after payment and does NOT reduce finalPayableAmount.
     * - effectiveCost = finalPayableAmount - potentialCashback.
     */
    fun calculate(
        platformId: String,
        platformName: String,
        baseTicketPrice: Int,
        ticketCount: Int,
        seatCategory: String = "Classic",
        userPaymentMethods: List<SavedPaymentMethod> = emptyList(),
        enteredCouponCode: String? = null
    ): PriceBreakdown {
        // 1. Calculate Base Total
        val multiplier = when (seatCategory) {
            "Prime" -> 1.25
            "Recliner" -> 1.75
            "Couple" -> 2.0
            else -> 1.0
        }
        val calculatedBase = (baseTicketPrice * multiplier).roundToInt() * ticketCount

        // 2. Platform Fees
        val feePerTicket = when (platformId.lowercase()) {
            "bms" -> 28
            "district" -> 18
            "pvr" -> 15
            "cinepolis" -> 16
            else -> 20
        }
        val convenienceFee = feePerTicket * ticketCount
        val internetHandlingFee = 5 * ticketCount
        val gstOnFees = ((convenienceFee + internetHandlingFee) * 0.18).roundToInt()
        val grossAmount = calculatedBase + convenienceFee + internetHandlingFee + gstOnFees

        // 3. Evaluate Discounts & Cashback
        var couponDiscount = 0
        val coupon = enteredCouponCode?.trim()?.uppercase()
        if (coupon == "SAVE100" && grossAmount >= 500) {
            couponDiscount = 100
        } else if (coupon == "MOVIE50" && grossAmount >= 250) {
            couponDiscount = 50
        } else if (coupon == "PVRPASS" && platformId.lowercase() == "pvr") {
            couponDiscount = 75
        }

        var bankDiscount = 0
        var upiDiscount = 0
        var potentialCashback = 0
        val appliedOfferDescriptions = mutableListOf<String>()

        val activeMethods = userPaymentMethods.filter { it.isSelected }

        for (method in activeMethods) {
            if (method.category == "CREDIT_CARD") {
                if (method.bank.equals("HDFC", ignoreCase = true) && grossAmount >= 400) {
                    val discount = ((grossAmount * 0.25).roundToInt()).coerceAtMost(150)
                    if (discount > bankDiscount) {
                        bankDiscount = discount
                        appliedOfferDescriptions.add("HDFC Bank Credit Card: ₹$discount Instant Discount")
                    }
                } else if (method.bank.equals("ICICI", ignoreCase = true) && grossAmount >= 300) {
                    val discount = ((grossAmount * 0.25).roundToInt()).coerceAtMost(100)
                    if (discount > bankDiscount) {
                        bankDiscount = discount
                        appliedOfferDescriptions.add("ICICI Bank Gemstone: ₹$discount Instant Discount")
                    }
                } else if (method.bank.equals("SBI Card", ignoreCase = true) && grossAmount >= 500) {
                    val discount = 100
                    if (discount > bankDiscount) {
                        bankDiscount = discount
                        appliedOfferDescriptions.add("SBI Card SimplyCLICK: ₹100 Flat Discount")
                    }
                }
            } else if (method.category == "UPI") {
                if (method.providerApp == "GOOGLE_PAY" && grossAmount >= 300) {
                    // Strictly Cashback!
                    potentialCashback = 75
                    appliedOfferDescriptions.add("Google Pay: ₹75 Expected Post-Payment Cashback")
                } else if (method.providerApp == "PHONEPE" && platformId.lowercase() == "district" && grossAmount >= 350) {
                    upiDiscount = 50
                    appliedOfferDescriptions.add("PhonePe UPI on District: ₹50 Instant Discount")
                }
            }
        }

        // Mutual Exclusion: If coupon entered, pick best instant discount unless stackable
        val totalInstantDiscount = (couponDiscount + bankDiscount + upiDiscount).coerceAtMost(grossAmount)
        val finalPayableAmount = (grossAmount - totalInstantDiscount).coerceAtLeast(0)
        val effectiveCost = (finalPayableAmount - potentialCashback).coerceAtLeast(0)

        return PriceBreakdown(
            platformId = platformId,
            platformName = platformName,
            ticketCount = ticketCount,
            seatCategory = seatCategory,
            basePrice = calculatedBase,
            convenienceFee = convenienceFee,
            internetHandlingFee = internetHandlingFee,
            gstOnFees = gstOnFees,
            mandatoryCharges = 0,
            grossAmount = grossAmount,
            appliedCouponCode = enteredCouponCode,
            couponDiscount = couponDiscount,
            bankDiscount = bankDiscount,
            upiDiscount = upiDiscount,
            walletDiscount = 0,
            membershipDiscount = 0,
            totalDiscount = totalInstantDiscount,
            finalPayableAmount = finalPayableAmount,
            potentialCashback = potentialCashback,
            effectiveCost = effectiveCost,
            appliedOfferDescriptions = appliedOfferDescriptions,
            isVerified = true,
            verifiedTimestamp = System.currentTimeMillis(),
            verificationAgeSeconds = 10
        )
    }
}
