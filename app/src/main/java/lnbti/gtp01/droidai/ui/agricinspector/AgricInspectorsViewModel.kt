package lnbti.gtp01.droidai.ui.agricinspector

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import lnbti.gtp01.droidai.models.AgricInspector
import lnbti.gtp01.droidai.repository.AgricInspectorRepository
import javax.inject.Inject

/**
 * ViewModel for managing agricultural inspectors data.
 *
 * This ViewModel is responsible for fetching and managing agricultural inspectors data
 * from the repository and providing it to the UI.
 */
@HiltViewModel
class AgricInspectorsViewModel @Inject constructor(
    private val agricInspectorRepository: AgricInspectorRepository
) : ViewModel() {
    private val _inspectorsLiveData = MutableLiveData<List<AgricInspector>?>()
    val inspectorsLiveData: MutableLiveData<List<AgricInspector>?> = _inspectorsLiveData

    /**
     * Initializes the ViewModel by fetching the list of agricultural inspectors.
     */
    init {
        getAgriInspectorList()
    }

    /**
     * Fetches the list of agricultural inspectors from the repository.
     */
    fun getAgriInspectorList() {
        viewModelScope.launch {
            try {
                val inspectorsResponse = agricInspectorRepository.getInspectors()
                val inspectors = inspectorsResponse.body()

                if (inspectors != null) {
                    // Update the LiveData with the list of inspectors
                    _inspectorsLiveData.value = inspectors
                    Log.d("AgricInspectorsViewModel", "+++ Inspectors $inspectors")
                } else {
                    Log.d("AgricInspectorsViewModel", "Inspectors response body is null")
                }
            } catch (e: Exception) {
                // Handle the exception here
                Log.e("AgricInspectorsViewModel", "Error: ${e.message}", e)
            }
        }
    }
}