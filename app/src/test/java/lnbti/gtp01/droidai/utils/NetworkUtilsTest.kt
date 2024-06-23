package lnbti.gtp01.droidai.utils


import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NetworkUtilsTest {

    @Mock
    private lateinit var mockConnectivityManager: ConnectivityManager

    @Before
    fun setup() {
        mockConnectivityManager = mock(ConnectivityManager::class.java)

        // Inject the mock ConnectivityManager before each test
        NetworkUtils.init(mockConnectivityManager)
    }

    @Test
    fun `test isNetworkAvailable with network available`() {
        // Mock a connected network with cellular capabilities
        val mockNetwork = mock(Network::class.java)
        val mockNetworkCapabilities = mock(NetworkCapabilities::class.java)
        `when`(mockConnectivityManager.activeNetwork).thenReturn(mockNetwork)
        `when`(mockConnectivityManager.getNetworkCapabilities(mockNetwork)).thenReturn(mockNetworkCapabilities)
        `when`(mockNetworkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)).thenReturn(true)

        assertTrue(NetworkUtils.isNetworkAvailable())
    }

    @Test
    fun `test isNetworkAvailable with no network available`() {
        // Mock no active network
        `when`(mockConnectivityManager.activeNetwork).thenReturn(null)

        assertFalse(NetworkUtils.isNetworkAvailable())
    }
}