package lnbti.gtp01.droidai.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class LanguageManagerTest {

    @Mock
    private lateinit var context: Context

    private lateinit var resources: Resources
    private lateinit var languageManager: LanguageManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        resources = mock(Resources::class.java)
        `when`(context.resources).thenReturn(resources)
        languageManager = LanguageManager(context)
        val sharedPreferences = mock<SharedPreferences>()

        // Initialize SharedPreferencesManager with the mocked SharedPreferences
        SharedPreferencesManager.init(sharedPreferences)
    }

    @Test
    fun testLoadLanguage() {
        // Mocking selected language
        val selectedLanguage = "en"
        `when`(SharedPreferencesManager.getSelectedLanguage()).thenReturn(selectedLanguage)

        // Call loadLanguage
        languageManager.loadLanguage()

        // Verify if the locale is set correctly
        assertEquals(Locale(selectedLanguage), Locale.getDefault())
    }
}