package lnbti.gtp01.droidai.ui.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    // Rule to swap the background executor used by the Architecture Components with a different one which executes each task synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock the MutableLiveData objects
    @Mock
    lateinit var selectedLanguageObserver: Observer<String?>

    @Mock
    lateinit var shouldSelectEnglishObserver: Observer<Boolean?>

    @Mock
    lateinit var shouldSelectSinhalaObserver: Observer<Boolean?>

    @Mock
    lateinit var shouldSelectJapaneseObserver: Observer<Boolean?>

    @Mock
    lateinit var titleLabelObserver: Observer<String?>

    @Mock
    lateinit var languageLabelObserver: Observer<String?>

    @Mock
    lateinit var changePasswordLabelObserver: Observer<String?>

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        // Initialize the ViewModel
        viewModel = SettingsViewModel()

        // Observe LiveData objects
        viewModel.selectedLanguage.observeForever(selectedLanguageObserver)
        viewModel.shouldSelectEnglish.observeForever(shouldSelectEnglishObserver)
        viewModel.shouldSelectSinhala.observeForever(shouldSelectSinhalaObserver)
        viewModel.shouldSelectJapanese.observeForever(shouldSelectJapaneseObserver)
        viewModel.titleLabel.observeForever(titleLabelObserver)
        viewModel.languageLabel.observeForever(languageLabelObserver)
        viewModel.changePasswordLabel.observeForever(changePasswordLabelObserver)
    }

    @Test
    fun testSetSelectedLanguage() {
        // Test setting selected language
        viewModel.setSelectedLanguage("en")

        // Verify that correct language is set
        assertEquals("en", viewModel.selectedLanguage.value)
        assertEquals(true, viewModel.shouldSelectEnglish.value)
        assertEquals(false, viewModel.shouldSelectSinhala.value)
        assertEquals(false, viewModel.shouldSelectJapanese.value)
    }

    @Test
    fun testSetSelectedLanguageLabels() {
        // Test setting selected language labels
        viewModel.setSelectedLanguageLabels("Title", "Language", "Change Password")

        // Verify that labels are set correctly
        assertEquals("Title", viewModel.titleLabel.value)
        assertEquals("Language", viewModel.languageLabel.value)
        assertEquals("Change Password", viewModel.changePasswordLabel.value)
    }

    @Test
    fun testOnEnglishLayoutClicked() {
        // Test click event for English layout
        viewModel.onEnglishLayoutClicked()

        // Verify that English layout click sets correct values
        assertEquals("en", viewModel.selectedLanguage.value)
        assertEquals(true, viewModel.shouldSelectEnglish.value)
        assertEquals(false, viewModel.shouldSelectSinhala.value)
        assertEquals(false, viewModel.shouldSelectJapanese.value)
    }

    @Test
    fun testOnSinhalaLayoutClicked() {
        // Test click event for Sinhala layout
        viewModel.onSinhalaLayoutClicked()

        // Verify that Sinhala layout click sets correct values
        assertEquals("si", viewModel.selectedLanguage.value)
        assertEquals(false, viewModel.shouldSelectEnglish.value)
        assertEquals(true, viewModel.shouldSelectSinhala.value)
        assertEquals(false, viewModel.shouldSelectJapanese.value)
    }

    @Test
    fun testOnJapaneseLayoutClicked() {
        // Test click event for Japanese layout
        viewModel.onJapaneseLayoutClicked()

        // Verify that Japanese layout click sets correct values
        assertEquals("ja", viewModel.selectedLanguage.value)
        assertEquals(false, viewModel.shouldSelectEnglish.value)
        assertEquals(false, viewModel.shouldSelectSinhala.value)
        assertEquals(true, viewModel.shouldSelectJapanese.value)
    }

    // Clean up after each test
    @After
    fun tearDown() {
        viewModel.selectedLanguage.removeObserver(selectedLanguageObserver)
        viewModel.shouldSelectEnglish.removeObserver(shouldSelectEnglishObserver)
        viewModel.shouldSelectSinhala.removeObserver(shouldSelectSinhalaObserver)
        viewModel.shouldSelectJapanese.removeObserver(shouldSelectJapaneseObserver)
        viewModel.titleLabel.removeObserver(titleLabelObserver)
        viewModel.languageLabel.removeObserver(languageLabelObserver)
        viewModel.changePasswordLabel.removeObserver(changePasswordLabelObserver)
    }
}