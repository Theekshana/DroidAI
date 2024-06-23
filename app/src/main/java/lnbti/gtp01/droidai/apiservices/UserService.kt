package lnbti.gtp01.droidai.apiservices

import lnbti.gtp01.droidai.constants.EndpointConfig.LOGIN_ENDPOINT
import lnbti.gtp01.droidai.constants.EndpointConfig.REGISTER_ENDPOINT
import lnbti.gtp01.droidai.models.LoginRequest
import lnbti.gtp01.droidai.models.LoginResponse
import lnbti.gtp01.droidai.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST(LOGIN_ENDPOINT)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST(REGISTER_ENDPOINT)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<LoginResponse>
}