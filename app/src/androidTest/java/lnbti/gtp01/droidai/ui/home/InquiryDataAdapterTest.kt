package lnbti.gtp01.droidai.ui.home

import lnbti.gtp01.droidai.models.Inquiry
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Unit tests for the [InquiryDataAdapter] class.
 */
@RunWith(MockitoJUnitRunner::class)
class InquiryDataAdapterTest {

    // Instantiate the adapter to be tested
    private lateinit var adapter: InquiryDataAdapter

    // Mock data for testing
    private val mockInquiryList = listOf(
        Inquiry(
            "911111111v",
            "https://example.com/image1.jpg",
            "Colombo",
            "Test Answer",
            "3 months",
            "false",
            "Problem 1",
            "2024-01-27T07:54:55.730Z",
            "1"
        ),

        Inquiry(
            "911111111v",
            "https://example.com/image1.jpg",
            "Colombo",
            "No Answer",
            "3 months",
            "false",
            "Problem 2",
            "2024-01-27T07:54:55.730Z",
            "2"
        )
        //Inquiry("userNIC1", "https://example.com/image1.jpg", "Colombo", "Problem 1", "No Answer", "2024-02-07", "1"),
        //Inquiry("userNIC1", "https://example.com/image1.jpg", "Problem 2", "2024-02-07", "2")
    )

    /**
     * Sets up the environment for each test case by initializing the adapter.
     */
    @Before
    fun setUp() {
        adapter = InquiryDataAdapter()
    }

    /**
     * Tests the [InquiryDataAdapter.submitSortedList] method.
     */
    @Test
    fun testSubmitSortedList() {
        // Submit mock data
        adapter.submitSortedList(mockInquiryList)

        // Get the current list from the adapter
        val currentList = adapter.differ.currentList

        // Assert that the list is sorted correctly
        assertEquals("Problem 1", currentList[0].problem)
        assertEquals("Problem 2", currentList[1].problem)
    }

    /**
     * Tests the [InquiryDataAdapter.getItemCount] method.
     */
    @Test
    fun testGetItemCount() {
        // Submit mock data
        adapter.submitSortedList(mockInquiryList)

        // Assert that getItemCount returns the correct count
        assertEquals(2, adapter.itemCount)
    }

    /**
     * Tests the [InquiryDataAdapter.formatDate] method.
     */
    @Test
    fun testFormatDate() {
        // Mock input date and time string
        val inputDateAndTime = "2024-01-01T09:00:00.000Z"

        // Create a mock InquiryDataAdapter object
        val adapter = InquiryDataAdapter()

        // Call the formatDate function with the mock input date and time string
        val formattedDate = adapter.formatDate(inputDateAndTime)

        // Create a SimpleDateFormat object to parse the formatted date string
        val dateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

        // Set the time zone of the date format to Asia/Tokyo
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Tokyo")

        // Parse the formatted date string to obtain a Date object
        val expectedDate = dateFormat.parse("01/01/2024 06:00 PM")

        // Convert the expected date to a formatted string using the same date format
        val expectedFormattedDate = expectedDate?.let { dateFormat.format(it) }

        // Assert that the formatted date matches the expected formatted date
        assertEquals(expectedFormattedDate, formattedDate)
    }
}