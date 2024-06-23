package lnbti.gtp01.droidai.repository

import kotlinx.coroutines.runBlocking
import lnbti.gtp01.droidai.apiservices.UserService
import lnbti.gtp01.droidai.models.LoginRequest
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.models.RegisterRequest
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryImplTest {

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var userRepositoryImpl: UserRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userService = mock(UserService::class.java)
    }

    // Given
    private val firebaseToken = "12345"
    private val userName = "Charith"
    private val password = "Charith@1234"
    private val nic = "911240807v"
    private val firstName = "Charith"
    private val lastName = "Vithanage"
    private val contactNo = "0712919248"

    private val loginRequest = LoginRequest(firebaseToken, userName, password)
    private val registerRequest =
        RegisterRequest(nic, firstName, lastName, contactNo, userName, password)
    private val expectedSuccessResponse = Response.success(
        LoginResponse(
            true,
            "Success"
        )
    )

    @Test
    fun `test login successfully`() {
        runBlocking {

            `when`(userRepositoryImpl.login(firebaseToken, userName, password)).thenReturn(
                LoginResponse(
                    true,
                    "Success"
                )
            )
            // When
            val result = userRepositoryImpl.login(firebaseToken, userName, password)

            // Then
            assertEquals(expectedSuccessResponse.body()?.success, result?.success)
        }
    }

    @Test
    fun `test login fail`() {
        runBlocking {

            `when`(userRepositoryImpl.login(firebaseToken, userName, password)).thenReturn(
                LoginResponse(
                    false,
                    "No User Found"
                )
            )
            // When
            val result = userRepositoryImpl.login(firebaseToken, userName, password)

            // Then
            assertNotNull(result)
            assertEquals(result?.message, "No User Found")
        }
    }

    @Test
    fun `test login fail with server error`() {
        runBlocking {

            `when`(userRepositoryImpl.login(firebaseToken, userName, password)).thenReturn(
                LoginResponse(
                    false,
                    "Server Error"
                )
            )

            // When
            val result = userRepositoryImpl.login(firebaseToken, userName, password)
            // Then
            assertNotNull(result)
            assertEquals(result!!.message, "Server Error")
            assertFalse(result.success)

        }
    }

    @Test
    fun `test loginToTheServer success response`() {

        // Mock the behavior of the loginUser function
        runBlocking {
            `when`(userService.loginUser(loginRequest)).thenReturn(expectedSuccessResponse)
            // When
            val response = userService.loginUser(loginRequest)
            // Then
            assertNotNull(response)
            assertTrue(response.isSuccessful)
            assertEquals(200, (response.code()))

        }
    }

    @Test
    fun `test loginToTheServer fail with server error`() = runBlocking {
        `when`(userService.loginUser(loginRequest)).thenReturn(
            Response.error(
                500,
                ResponseBody.create(null, "")
            )
        )
        // When
        val response = userService.loginUser(loginRequest)
        // Then
        assertNotNull(response)
        assertFalse(response.isSuccessful)
        assertEquals(500, (response.code()))
    }

    @Test
    fun `test register successfully`() {
        runBlocking {

            `when`(
                userRepositoryImpl.register(
                    nic,
                    firstName,
                    lastName,
                    contactNo,
                    userName,
                    password
                )
            ).thenReturn(
                LoginResponse(
                    true,
                    "Success"
                )
            )
            // When
            val result =
                userRepositoryImpl.register(nic, firstName, lastName, contactNo, userName, password)

            // Then
            assertEquals(expectedSuccessResponse.body()?.success, result?.success)
        }
    }

    @Test
    fun `test register fail`() {
        runBlocking {

            `when`(
                userRepositoryImpl.register(
                    nic,
                    firstName,
                    lastName,
                    contactNo,
                    "Kalana",
                    password
                )
            ).thenReturn(
                LoginResponse(
                    false,
                    "Username Exist"
                )
            )
            // When
            val result =
                userRepositoryImpl.register(nic, firstName, lastName, contactNo, "Kalana", password)

            // Then
            assertFalse(result!!.success)
            assertEquals(result.message, "Username Exist")
        }
    }

    @Test
    fun `test registerToTheServer success response`() {

        // Mock the behavior of the loginUser function
        runBlocking {
            `when`(userService.registerUser(registerRequest)).thenReturn(expectedSuccessResponse)
            // When
            val response = userService.registerUser(registerRequest)
            // Then
            assertNotNull(response)
            assertTrue(response.isSuccessful)
            assertEquals(200, (response.code()))

        }
    }

    @Test
    fun `test registerToTheServer fail with server error`() = runBlocking {
        `when`(userService.registerUser(registerRequest)).thenReturn(
            Response.error(
                500,
                ResponseBody.create(null, "")
            )
        )
        // When
        val response = userService.registerUser(registerRequest)
        // Then
        assertNotNull(response)
        assertFalse(response.isSuccessful)
        assertEquals(500, (response.code()))
    }
}
