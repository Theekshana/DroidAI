package lnbti.gtp01.droidai.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationsTest {

    @Test
    fun `test isMobileNumberValid with valid mobile number`() {
        assertTrue(Validations.isMobileNumberValid("1234567890"))
    }

    @Test
    fun `test isMobileNumberValid with invalid mobile number`() {
        assertFalse(Validations.isMobileNumberValid("123")) // Less than 10 digits
        assertFalse(Validations.isMobileNumberValid("")) // Empty
        assertFalse(Validations.isMobileNumberValid(null)) // Null
        assertFalse(Validations.isMobileNumberValid("abcdefghij")) // Non-numeric
    }

    @Test
    fun `test isPasswordValid with valid password`() {
        assertTrue(Validations.isPasswordValid("password123"))
    }

    @Test
    fun `test isPasswordValid with invalid password`() {
        assertFalse(Validations.isPasswordValid("")) // Empty
        assertFalse(Validations.isPasswordValid(null)) // Null
        assertFalse(Validations.isPasswordValid("pass")) // Less than 6 characters
    }

    @Test
    fun `test isUserNameValid`() {
        assertTrue(Validations.isUserNameValid("username"))
        assertFalse(Validations.isUserNameValid("")) // Empty
        assertFalse(Validations.isUserNameValid(null)) // Null
    }

    @Test
    fun `test isNICValid with valid NIC`() {
        assertTrue(Validations.isNICValid("123456789012")) // New NIC format
        assertTrue(Validations.isNICValid("123456789v")) // Old NIC format
        assertTrue(Validations.isNICValid("123456789X")) // Old NIC format
    }

    @Test
    fun `test isNICValid with invalid NIC`() {
        assertFalse(Validations.isNICValid("")) // Empty
        assertFalse(Validations.isNICValid(null)) // Null
        assertFalse(Validations.isNICValid("123456")) // Less than 10 characters
        assertFalse(Validations.isNICValid("1234567890123")) // More than 12 characters
        assertFalse(Validations.isNICValid("12345678X")) // Old NIC format with invalid last character
        assertFalse(Validations.isNICValid("1234567890")) // Old NIC format with no 'v' or 'x' at the end
    }
}