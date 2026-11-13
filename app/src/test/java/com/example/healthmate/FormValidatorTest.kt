package com.example.healthmate

import com.example.healthmate.ui.addedit.FormValidator
import com.example.healthmate.util.DateUtils
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FormValidatorTest {

    // TC-12: a normal past date is accepted
    @Test
    fun validateDate_validPastDate_returnsNull() {
        assertNull(FormValidator.validateDate(DateUtils.daysAgo(2)))
    }

    // TC-13: wrong formats and empty text are rejected
    @Test
    fun validateDate_badFormat_returnsError() {
        assertNotNull(FormValidator.validateDate(""))
        assertNotNull(FormValidator.validateDate("13/11/2026"))
        assertNotNull(FormValidator.validateDate("abc"))
    }

    // TC-14: tomorrow is rejected (daysAgo(-1) = one day ahead)
    @Test
    fun validateDate_futureDate_returnsError() {
        assertNotNull(FormValidator.validateDate(DateUtils.daysAgo(-1)))
    }

    // TC-15: an impossible date such as 31 February is rejected
    @Test
    fun validateDate_impossibleDate_returnsError() {
        assertNotNull(FormValidator.validateDate("2026-02-31"))
    }

    // TC-16: a valid whole number is accepted
    @Test
    fun validateNumber_validValue_returnsNull() {
        assertNull(FormValidator.validateNumber("7850", "Steps", FormValidator.MAX_STEPS))
    }

    // TC-17: empty input is rejected
    @Test
    fun validateNumber_empty_returnsError() {
        assertNotNull(FormValidator.validateNumber("", "Steps", FormValidator.MAX_STEPS))
    }

    // TC-18: letters and decimals are rejected
    @Test
    fun validateNumber_notWholeNumber_returnsError() {
        assertNotNull(FormValidator.validateNumber("abc", "Steps", FormValidator.MAX_STEPS))
        assertNotNull(FormValidator.validateNumber("12.5", "Steps", FormValidator.MAX_STEPS))
    }

    // TC-19: negative numbers are rejected
    @Test
    fun validateNumber_negative_returnsError() {
        assertNotNull(FormValidator.validateNumber("-5", "Steps", FormValidator.MAX_STEPS))
    }

    // TC-20: numbers above the limit are rejected
    @Test
    fun validateNumber_tooHigh_returnsError() {
        assertNotNull(FormValidator.validateNumber("999999", "Steps", FormValidator.MAX_STEPS))
    }
}