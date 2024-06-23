package lnbti.gtp01.droidai.ui.dealerlocations

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import lnbti.gtp01.droidai.models.Dealer
import javax.inject.Inject

/**
 * Dealer Locations Fragment View Model
 */
@HiltViewModel
class DealerLocationsViewModel @Inject constructor() : ViewModel() {

    private val _dealerList = MutableLiveData<List<Dealer>>()
    val dealerList get() = _dealerList

    private var allUsersList: List<Dealer> = emptyList() // Initialize as empty list

    fun setAllDealers(list: List<Dealer>) {
        allUsersList = listOf()
        allUsersList = list
        _dealerList.value = allUsersList
    }

    fun filterUsersByDistrict(allValue: String, district: String) {
        if (district == allValue) {
            _dealerList.value = allUsersList
        } else {
            // Make sure allUsersList is initialized before filtering
            allUsersList.let {
                val filteredList = it.filter { it.district == district }
                _dealerList.value = filteredList
            }
        }
    }
}