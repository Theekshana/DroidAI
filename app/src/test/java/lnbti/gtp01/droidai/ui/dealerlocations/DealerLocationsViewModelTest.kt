package lnbti.gtp01.droidai.ui.dealerlocations

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import lnbti.gtp01.droidai.models.Dealer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class DealerLocationsViewModelTest {
    val dealers = listOf(
        Dealer("Dealer 1", "Address 1", "District 1", "0712345678", "url1"),
        Dealer("Dealer 2", "Address 2", "District 2", "0712345688", "url2"),
        Dealer("Dealer 3", "Address 3", "District 3", "0712345679", "url3")
    )

    val filteredDealers = listOf(
        Dealer("Dealer 1", "Address 1", "District 1", "0712345678", "url1")
    )

    // Run tasks synchronously
    @get:Rule
    val rule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var viewModel: DealerLocationsViewModel

    @Before
    fun setUp() {
        viewModel = DealerLocationsViewModel()
    }

    @Test
    fun `test setAllDealers`() {
        // Given

        // When
        viewModel.setAllDealers(dealers)

        // Then
        assertEquals(dealers, viewModel.dealerList.value)
    }

    @Test
    fun `test filterUsersByDistrict with allValue`() {
        // Given
        val allValue = "All"


        viewModel.setAllDealers(dealers)

        // When
        viewModel.filterUsersByDistrict(allValue, "All")

        // Then
        assertEquals(dealers, viewModel.dealerList.value)
    }

    @Test
    fun `test filterUsersByDistrict with valid district`() {
        // Given
        val district = "District 2"

        viewModel.setAllDealers(dealers)

        // When
        viewModel.filterUsersByDistrict("All", district)

        // Then
        val filteredDealers = dealers.filter { it.district == district }
        assertEquals(filteredDealers, viewModel.dealerList.value)
    }

    @Test
    fun `test filterUsersByDistrict with invalid district`() {
        // Given
        val invalidDistrict = "Nonexistent District"

        viewModel.setAllDealers(dealers)

        // When
        viewModel.filterUsersByDistrict("All", invalidDistrict)

        // Then
        assertEquals(emptyList<Dealer>(), viewModel.dealerList.value)
    }
}