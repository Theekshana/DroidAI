package lnbti.gtp01.droidai.utils

import android.content.SharedPreferences
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SharedPreferencesManagerTest {

    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        // Mock SharedPreferences
        sharedPreferences = Mockito.mock<SharedPreferences>()

        // Mock SharedPreferences.Editor
        editor = Mockito.mock()
        `when`(sharedPreferences.edit()).thenReturn(editor)

        // Initialize SharedPreferencesManager with the mocked SharedPreferences
        SharedPreferencesManager.init(sharedPreferences)
    }

    @Test
    fun `test getSelectedLanguage returns default language`() {
        // Given
        `when`(sharedPreferences.getString(any(), any())).thenReturn(StringConstants.SINHALA)

        // When
        val selectedLanguage = SharedPreferencesManager.getSelectedLanguage()

        // Then
        assertEquals(StringConstants.SINHALA, selectedLanguage)
    }

    @Test
    fun `test updateSelectedLanguage`() {
        // Given
        val jsonString = "sinhala"

        // When
        SharedPreferencesManager.updateSelectedLanguage(jsonString)

        // Then
        verify(editor).putString(PreferenceKeys.LANGUAGE, jsonString)
        verify(editor).apply()
    }

    @Test
    fun `test savePreference`() {
        // Given
        val key = "key"
        val jsonString = "value"

        // When
        SharedPreferencesManager.savePreference(key, jsonString)

        // Then
        verify(editor).putString(key, jsonString)
        verify(editor).apply()
    }

    @Test
    fun `test getPreference`() {
        // Given
        val key = "key"
        val expectedValue = "value"
        `when`(sharedPreferences.getString(key, null)).thenReturn(expectedValue)

        // When
        val retrievedValue = SharedPreferencesManager.getPreference(key)

        // Then
        assertEquals(expectedValue, retrievedValue)
    }

    @Test
    fun `test getPreferenceBool`() {
        // Given
        val key = "key"
        val expectedValue = true
        `when`(sharedPreferences.getBoolean(key, false)).thenReturn(expectedValue)

        // When
        val retrievedValue = SharedPreferencesManager.getPreferenceBool(key)

        // Then
        assertEquals(expectedValue, retrievedValue)
    }

    @Test
    fun `test savePreferenceBool`() {
        // Given
        val key = "key"
        val value = true

        // When
        SharedPreferencesManager.savePreferenceBool(key, value)

        // Then
        verify(editor).putBoolean(key, value)
        verify(editor).apply()
    }

    @Test
    fun `test clearAllPref`() {
        // When
        SharedPreferencesManager.clearAllPref()

        // Then
        verify(editor).clear()
        verify(editor).apply()
    }
}