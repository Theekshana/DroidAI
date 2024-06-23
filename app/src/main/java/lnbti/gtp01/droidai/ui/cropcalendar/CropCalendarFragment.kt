package lnbti.gtp01.droidai.ui.cropcalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentCropCalendarBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.models.CalendarEventObject
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showConfirmAlertDialogInFragment
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.savePreference
import lnbti.gtp01.droidai.utils.Utils.Companion.getEvents
import java.text.SimpleDateFormat
import java.util.Date

/**
 * A fragment that displays crop calendar.
 *
 * This fragment includes custom toolbar behavior to control the appearance and behavior of the
 * toolbar in the context of notifications.
 */
class CropCalendarFragment : Fragment() {
    lateinit var binding: FragmentCropCalendarBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var viewModel: CropCalendarViewModel
    private lateinit var eventAdapter: CalendarEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentCropCalendarBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@CropCalendarFragment

            ViewModelProvider(requireActivity())[CropCalendarViewModel::class.java].apply {
                //Data binding
                viewModel = this
            }

            eventAdapter = CalendarEventAdapter(getEvents(),
                object : CalendarEventAdapter.OnItemLongClickListener {
                    override fun onLongClick(item: CalendarEventObject) {

                        showConfirmAlertDialogInFragment(this@CropCalendarFragment,
                            getString(R.string.delete_event_cofirmation),
                            object : ConfirmDialogButtonClickListener {
                                override fun onPositiveButtonClick() {
                                    deleteEvent(item)
                                }

                                override fun onNegativeButtonClick() {
                                }
                            }
                        )
                    }

                })
            eventRecyclerView.adapter = eventAdapter
            // Initialize CalendarView
            calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
                viewModel.setSelectedDate("$year-${month + 1}-$dayOfMonth")
            }

            val sdf = SimpleDateFormat("yyyy-M-dd")
            val currentDate = sdf.format(Date())
            viewModel.setSelectedDate(currentDate)
            cardViewActivityCropCalenderAdd.setOnClickListener {
                openAddEvent()
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CROP_CALENDAR_FRAGMENT)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }
    }

    override fun onResume() {
        super.onResume()
        eventAdapter.updateData(getEvents())
    }

    fun openAddEvent() {
        if (viewModel.selectedDate.value!!.isNotEmpty()) {
            findNavController().navigate(
                CropCalendarFragmentDirections.actionCropCalendarFragmentToAddCalendarEventFragment()
            )
        } else {
            sharedViewModel.showErrorDialog(getString(R.string.date_select_error))
        }
    }

    fun deleteEvent(event: CalendarEventObject) {
        val events = getEvents().filter { it != event }.toMutableList()
        val json = Gson().toJson(events)
        savePreference(PreferenceKeys.EVENTS, json, object : SuccessListener {
            override fun onSuccess() {
                eventAdapter.updateData(getEvents())
            }
        })
    }
}