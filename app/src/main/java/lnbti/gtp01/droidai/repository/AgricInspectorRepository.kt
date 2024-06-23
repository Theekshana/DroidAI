package lnbti.gtp01.droidai.repository

import lnbti.gtp01.droidai.apiservices.AgricInspectorService
import javax.inject.Inject

/**
 * Repository for managing agricultural inspector data.
 *
 * This repository is responsible for handling operations related to agricultural inspector data,
 * such as fetching inspectors from a remote service.
 */
class AgricInspectorRepository @Inject constructor(
    private val apiService: AgricInspectorService
) {
    suspend fun getInspectors() = apiService.getInspectors()

}

