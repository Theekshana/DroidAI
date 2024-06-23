package lnbti.gtp01.droidai.di

import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import lnbti.gtp01.droidai.apiservices.AgricInspectorService
import lnbti.gtp01.droidai.apiservices.InquiryApiService
import lnbti.gtp01.droidai.apiservices.UserService
import lnbti.gtp01.droidai.apiservices.WeatherApiService
import lnbti.gtp01.droidai.constants.AgricInspectorApiConfig
import lnbti.gtp01.droidai.constants.EndpointConfig
import lnbti.gtp01.droidai.repository.InquiryRepository
import lnbti.gtp01.droidai.repository.InquiryRepositoryImpl
import lnbti.gtp01.droidai.repository.UserRepository
import lnbti.gtp01.droidai.repository.UserRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * NetworkModule object class is responsible for providing Dependency Injection (DI) components
 * for network operations in the context of weather information retrieval. This includes
 * providing the base URL for weather APIs, converter factories for JSON serialization,
 * an HTTP client, and the Retrofit instance configured for weather data API calls.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides the base URL for the weather API.
     *
     * @return String representing the base URL of the weather API.
     */
    @Singleton
    @Provides
    @Named("weatherApiBaseUrl")
    fun provideBaseUrl(): String {
        return EndpointConfig.WEATHER_API_BASE_URL
    }

    /**
     * Provides the base URL for the inquiry API.
     *
     * @return String representing the base URL of the inquiry API.
     */
    @Singleton
    @Provides
    @Named("inquiryBaseUrl")
    fun provideInquiryApiBaseUrl(): String {
        return EndpointConfig.INQUIRY_API_BASE_URL
    }

    /**
     * Provides a Gson Converter Factory for JSON serialization and deserialization.
     * Useful for converting JSON data from the weather API into Kotlin objects.
     *
     * @return Converter.Factory instance for Gson-based conversion.
     */
    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    /**
     * Provides an OkHttpClient instance. This client can be customized with interceptors,
     * timeouts, and other network configurations as needed.
     *
     * @return OkHttpClient instance for network operations.
     */
    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    /**
     * Provides a Retrofit instance for making network requests to the weather API.
     * Configured with the base URL, OkHttpClient, and a converter factory.
     *
     * @param baseUrl Base URL of the weather API.
     * @param okHttpClient OkHttpClient instance for network operations.
     * @param converterFactory Converter Factory for JSON conversion.
     *
     * @return Retrofit instance configured for weather API requests.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("weatherApiBaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): WeatherApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(WeatherApiService::class.java)
    }

    /**
     * Provides an instance of [InquiryApiService] with a specific base URL using Retrofit.
     *
     * @param baseUrl The base URL for the [InquiryApiService]. Should include the scheme (http or https).
     * @param okHttpClient The OkHttpClient instance for network operations.
     * @param converterFactory The Converter Factory for JSON conversion.
     *
     * @return An instance of [InquiryApiService] configured with the specified base URL.
     */
    @Singleton
    @Provides
    fun provideInquiryApiService(
        @Named("inquiryBaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): InquiryApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(InquiryApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(
        @Named("inquiryBaseUrl") baseUrl: String,
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): UserService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
            .create(UserService::class.java)
    }

    /**
     * Provides an instance of [InquiryRepository] for dependency injection.
     * This function creates and returns an instance of [InquiryRepositoryImpl] using the provided [InquiryApiService].
     *
     * @param inquiryApiService The [InquiryApiService] implementation used by the repository.
     * @param storage The Firebase storage instance used for storing data related to inquiries.
     * @return An instance of [InquiryRepository].
     */
    @Singleton
    @Provides
    fun provideInquiryRepository(
        inquiryApiService: InquiryApiService,
        storage: FirebaseStorage,
        ): InquiryRepository {
        return InquiryRepositoryImpl(inquiryApiService, storage)
    }

    /**
     * Provides an instance of FirebaseStorage for dependency injection.
     * This function creates and returns an instance of FirebaseStorage.
     *
     * @return An instance of FirebaseStorage.
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }


    @Singleton
    @Provides
    fun provideUserRepository(
        userService: UserService,
    ): UserRepository {
        return UserRepositoryImpl(userService)
    }


    @Singleton
    @Provides
    @Named("AgricInspectorRetrofit")
    fun provideAgricInspectorRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AgricInspectorApiConfig.DROID_AI_MOCK_API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideAgricInspectorService(@Named("AgricInspectorRetrofit") retrofit: Retrofit): AgricInspectorService {
        return retrofit.create(AgricInspectorService::class.java)
    }


}