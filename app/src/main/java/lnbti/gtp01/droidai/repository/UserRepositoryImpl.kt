package lnbti.gtp01.droidai.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lnbti.gtp01.droidai.apiservices.UserService
import lnbti.gtp01.droidai.constants.StringConstants.TAG
import lnbti.gtp01.droidai.models.ErrorBody
import lnbti.gtp01.droidai.models.LoginRequest
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.models.RegisterRequest
import lnbti.gtp01.droidai.utils.Utils.Companion.getErrorBodyFromResponse
import javax.inject.Inject

/**
 * User Repository
 */
class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
) : UserRepository {

    /**
     * Login Coroutines
     */
    override suspend fun login(
        firebaseToken: String?, userName: String?, password: String?,
    ): LoginResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext loginToTheServer(firebaseToken, userName, password)
        }
    }

    /**
     * Send Request to the server and get the response
     */
    override suspend fun loginToTheServer(
        firebaseToken: String?,
        userName: String?,
        password: String?,
    ): LoginResponse? {
        val response = userService.loginUser(LoginRequest(firebaseToken, userName, password))

        return try {
            if (response.isSuccessful) {
                response.body()
            } else {
                val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
                LoginResponse(false, errorObject.message)
            }
        } catch (e: Exception) {
            LoginResponse(false, e.message)
        }
    }

    override suspend fun register(
        nic: String?,
        firstName: String?,
        lastName: String?,
        contactNo: String?,
        userName: String?,
        password: String?,
    ): LoginResponse? {
        return withContext(Dispatchers.IO) {
            return@withContext registerToTheServer(
                nic,
                firstName,
                lastName,
                contactNo,
                userName,
                password
            )
        }
    }

    /**
     * Send Request to the server and get the response
     */
    override suspend fun registerToTheServer(
        nic: String?,
        firstName: String?,
        lastName: String?,
        contactNo: String?,
        userName: String?,
        password: String?,
    ): LoginResponse? {

        return try {
            val response = userService.registerUser(
                RegisterRequest(
                    nic,
                    firstName,
                    lastName,
                    contactNo,
                    userName,
                    password
                )
            )
            Log.d(TAG, "Response Object " + response.body().toString())
            if (response.isSuccessful) {
                response.body()
            } else {
                val errorObject: ErrorBody = getErrorBodyFromResponse(response.errorBody())
                LoginResponse(false, errorObject.message)
            }
        } catch (e: Exception) {
            LoginResponse(false, e.message)
        }
    }
}