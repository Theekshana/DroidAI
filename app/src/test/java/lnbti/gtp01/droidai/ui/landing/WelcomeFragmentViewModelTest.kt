package lnbti.gtp01.droidai.ui.landing

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WelcomeFragmentViewModelTest {

    // Rule to make LiveData work on background thread
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Mocked Observers
    @Mock
    lateinit var selectedLanguageObserver: Observer<String?>

    @Mock
    lateinit var shouldSelectEnglishObserver: Observer<Boolean?>

    @Mock
    lateinit var shouldSelectSinhalaObserver: Observer<Boolean?>

    @Mock
    lateinit var shouldSelectJapaneseObserver: Observer<Boolean?>

    @Mock
    lateinit var contentLabelObserver: Observer<String?>

    @Mock
    lateinit var btnTextObserver: Observer<String?>

    // System under test
    private lateinit var viewModel: WelcomeFragmentViewModel

    @Before
    fun setup() {
        viewModel = WelcomeFragmentViewModel()

        // Observe all LiveData in ViewModel
        viewModel.selectedLanguage.observeForever(selectedLanguageObserver)
        viewModel.shouldSelectEnglish.observeForever(shouldSelectEnglishObserver)
        viewModel.shouldSelectSinhala.observeForever(shouldSelectSinhalaObserver)
        viewModel.shouldSelectJapanese.observeForever(shouldSelectJapaneseObserver)
        viewModel.contentLabel.observeForever(contentLabelObserver)
        viewModel.btnText.observeForever(btnTextObserver)
    }

    @Test
    fun `test setSelectedLanguage`() {
        val language = "en"

        // Call the method to be tested
        viewModel.setSelectedLanguage(language)

        // Verify that LiveData are updated accordingly
        verify(selectedLanguageObserver).onChanged(language)
        verify(shouldSelectEnglishObserver).onChanged(true)
        verify(shouldSelectSinhalaObserver).onChanged(false)
        verify(shouldSelectJapaneseObserver).onChanged(false)
    }

    @Test
    fun `test setSelectedLanguageLabels`() {
        val contentLabel = "Content Label"
        val btnText = "Button Text"

        // Call the method to be tested
        viewModel.setSelectedLanguageLabels(contentLabel, btnText)

        // Verify that LiveData are updated accordingly
        verify(contentLabelObserver).onChanged(contentLabel)
        verify(btnTextObserver).onChanged(btnText)
    }

    @Test
    fun testOnEnglishLayoutClicked() {
        // Test click event for English layout
        viewModel.onEnglishLayoutClicked()

        // Verify that English layout click sets correct values
        Assertions.assertEquals("en", viewModel.selectedLanguage.value)
        Assertions.assertEquals(true, viewModel.shouldSelectEnglish.value)
        Assertions.assertEquals(false, viewModel.shouldSelectSinhala.value)
        Assertions.assertEquals(false, viewModel.shouldSelectJapanese.value)
    }

    @Test
    fun testOnSinhalaLayoutClicked() {
        // Test click event for Sinhala layout
        viewModel.onSinhalaLayoutClicked()

        // Verify that Sinhala layout click sets correct values
        Assertions.assertEquals("si", viewModel.selectedLanguage.value)
        Assertions.assertEquals(false, viewModel.shouldSelectEnglish.value)
        Assertions.assertEquals(true, viewModel.shouldSelectSinhala.value)
        Assertions.assertEquals(false, viewModel.shouldSelectJapanese.value)
    }

    @Test
    fun testOnJapaneseLayoutClicked() {
        // Test click event for Japanese layout
        viewModel.onJapaneseLayoutClicked()

        // Verify that Japanese layout click sets correct values
        Assertions.assertEquals("ja", viewModel.selectedLanguage.value)
        Assertions.assertEquals(false, viewModel.shouldSelectEnglish.value)
        Assertions.assertEquals(false, viewModel.shouldSelectSinhala.value)
        Assertions.assertEquals(true, viewModel.shouldSelectJapanese.value)
    }

    // Clean up after each test
    @After
    fun tearDown() {
        viewModel.selectedLanguage.removeObserver(selectedLanguageObserver)
        viewModel.shouldSelectEnglish.removeObserver(shouldSelectEnglishObserver)
        viewModel.shouldSelectSinhala.removeObserver(shouldSelectSinhalaObserver)
        viewModel.shouldSelectJapanese.removeObserver(shouldSelectJapaneseObserver)
        viewModel.contentLabel.removeObserver(contentLabelObserver)
        viewModel.btnText.removeObserver(btnTextObserver)
    }
}