package lnbti.gtp01.droidai.apiservices

import lnbti.gtp01.droidai.models.AgricInspector
import retrofit2.Response
import retrofit2.http.GET

/**
 * Service interface for fetching agricultural inspector data.
 *
 * This interface defines methods for fetching agricultural inspector data from a remote service.
 */
interface AgricInspectorService {

    /**
     * Fetches a list of agricultural inspectors from the remote service.
     *
     * @return A suspendable function that returns the response containing the list of agricultural inspectors.
     */
    @GET("hasitha-s/DroidAI-Mock-API/main/inspectors/AgricInspector.json")
    suspend fun getInspectors(): Response<List<AgricInspector>>

}

