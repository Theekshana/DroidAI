package lnbti.gtp01.droidai.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.gtp01.droidai.constants.StringConstants.ENGLISH
import lnbti.gtp01.droidai.constants.StringConstants.JAPANESE
import lnbti.gtp01.droidai.constants.StringConstants.SINHALA
import javax.inject.Inject

/**
 * Settings Fragment View Model
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
) : ViewModel() {

    private val _selectedLanguage = MutableLiveData<String?>(null)
    val selectedLanguage get() = _selectedLanguage

    private val _shouldSelectEnglish = MutableLiveData<Boolean?>(null)
    val shouldSelectEnglish get() = _shouldSelectEnglish

    private val _shouldSelectSinhala = MutableLiveData<Boolean?>(null)
    val shouldSelectSinhala get() = _shouldSelectSinhala

    private val _shouldSelectJapanese = MutableLiveData<Boolean?>(null)
    val shouldSelectJapanese get() = _shouldSelectJapanese

    private val _titleLabel = MutableLiveData<String?>(null)
    val titleLabel get() = _titleLabel

    private val _languageLabel = MutableLiveData<String?>(null)
    val languageLabel get() = _languageLabel

    private val _changePasswordLabel = MutableLiveData<String?>(null)
    val changePasswordLabel get() = _changePasswordLabel

    /**
     * Sets the selected language and updates related LiveData values.
     *
     * @param language The code representing the selected language.
     */
    fun setSelectedLanguage(language: String?) {
        _selectedLanguage.value = language
        run {
            _shouldSelectEnglish.value = language.equals(ENGLISH)
            _shouldSelectSinhala.value = language.equals(SINHALA)
            _shouldSelectJapanese.value = language.equals(JAPANESE)
        }
    }

    /**
     * Sets the label associated with the selected language.
     *
     * @param label The label to be associated with the selected language.
     */
    fun setSelectedLanguageLabels(
        titleLabel: String?,
        languageLabel: String?,
        changePasswordLabel: String?
    ) {
        _titleLabel.value = titleLabel
        _languageLabel.value = languageLabel
        _changePasswordLabel.value = changePasswordLabel
    }

    /**
     * Handles the click event for the English layout, setting the selected language to English.
     */
    fun onEnglishLayoutClicked() {
        // Handle the click event for the English layout
        _selectedLanguage.value = ENGLISH
        _shouldSelectJapanese.value = false
        _shouldSelectSinhala.value = false
        _shouldSelectEnglish.value = true
    }

    /**
     * Handles the click event for the Japanese layout, setting the selected language to Japanese.
     */
    fun onSinhalaLayoutClicked() {
        // Handle the click event for the Japanese layout
        _selectedLanguage.value = SINHALA
        _shouldSelectSinhala.value = true
        _shouldSelectEnglish.value = false
        _shouldSelectJapanese.value = false
    }

    /**
     * Handles the click event for the Japanese layout, setting the selected language to Japanese.
     */
    fun onJapaneseLayoutClicked() {
        // Handle the click event for the Japanese layout
        _selectedLanguage.value = JAPANESE
        _shouldSelectJapanese.value = true
        _shouldSelectEnglish.value = false
        _shouldSelectSinhala.value = false
    }
}