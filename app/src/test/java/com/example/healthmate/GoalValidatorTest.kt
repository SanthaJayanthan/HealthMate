package com.example.healthmate

import com.example.healthmate.ui.goals.GoalValidator
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class GoalValidatorTest {

    // TC-40: a normal goal is accepted
    @Test
    fun validate_validGoal_returnsNull() {
        assertNull(GoalValidator.validate("8000", "Steps", GoalValidator.MAX_STEPS))
    }

    // TC-41: empty input is rejected
    @Test
    fun validate_empty_returnsError() {
        assertNotNull(GoalValidator.validate("", "Steps", GoalValidator.MAX_STEPS))
    }

    // TC-42: letters and decimals are rejected
    @Test
    fun validate_notWholeNumber_returnsError() {
        assertNotNull(GoalValidator.validate("abc", "Steps", GoalValidator.MAX_STEPS))
        assertNotNull(GoalValidator.validate("12.5", "Steps", GoalValidator.MAX_STEPS))
    }

    // TC-43: zero and negative goals are rejected
    @Test
    fun validate_zeroOrNegative_returnsError() {
        assertNotNull(GoalValidator.validate("0", "Steps", GoalValidator.MAX_STEPS))
        assertNotNull(GoalValidator.validate("-100", "Steps", GoalValidator.MAX_STEPS))
    }

    // TC-44: goals above the maximum are rejected
    @Test
    fun validate_tooHigh_returnsError() {
        assertNotNull(GoalValidator.validate("999999", "Steps", GoalValidator.MAX_STEPS))
    }
}

