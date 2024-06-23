package lnbti.gtp01.droidai.ui.cropcalendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CropCalendarViewModel @Inject constructor(
) : ViewModel() {

    private val _selectedDate = MutableLiveData(
        ""
    )
    val selectedDate get() = _selectedDate

    fun setSelectedDate(selectedDate: String) {
        _selectedDate.value = selectedDate
    }
}