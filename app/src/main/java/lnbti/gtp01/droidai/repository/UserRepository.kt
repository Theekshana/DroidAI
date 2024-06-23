package lnbti.gtp01.droidai.repository

import android.util.Log
import com.google.gson.Gson
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
interface UserRepository {

    /**
     * Login Coroutines
     */
    suspend fun login(
        firebaseToken: String?, userName: String?, password: String?
    ): LoginResponse?

    /**
     * Send Request to the server and get the response
     */
    suspend fun loginToTheServer(firebaseToken: String?, userName: String?, password: String?): LoginResponse?

    suspend fun register(
        nic: String?,
        firstName: String?,
        lastName: String?,
        contactNo: String?,
        userName: String?,
        password: String?
    ): LoginResponse?

    /**
     * Send Request to the server and get the response
     */
    suspend fun registerToTheServer(
        nic: String?,
        firstName: String?,
        lastName: String?,
        contactNo: String?,
        userName: String?,
        password: String?
    ): LoginResponse?
}