package lnbti.gtp01.droidai.ui.cropcalendar

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Rule as Rule1

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CropCalendarViewModelTest {

    @get:Rule1
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<String>

    private lateinit var viewModel: CropCalendarViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = CropCalendarViewModel()
    }

    @Test
    fun `setSelectedDate sets selectedDate`() {
        // Given
        val selectedDate = "2024-04-07"

        // Observe selectedDate
        viewModel.selectedDate.observeForever(observer)

        // When
        viewModel.setSelectedDate(selectedDate)

        // Then
        Mockito.verify(observer).onChanged(selectedDate)
        assertEquals(selectedDate, viewModel.selectedDate.value)
    }
}