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
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentAddEventBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.models.CalendarEventObject
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.savePreference
import lnbti.gtp01.droidai.utils.Utils.Companion.getEvents

class AddCalendarEventFragment : Fragment() {
    lateinit var binding: FragmentAddEventBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var cropCalendarViewModel: CropCalendarViewModel
    private lateinit var viewModel: CropCalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentAddEventBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@AddCalendarEventFragment

            ViewModelProvider(requireActivity())[CropCalendarViewModel::class.java].apply {
                //Data binding
                viewModel = this
            }

            ViewModelProvider(requireActivity())[CropCalendarViewModel::class.java].apply {
                cropCalendarViewModel = this
                editTextActivityAddEventDate.setText(selectedDate.value.toString())
            }

            buttonActivityAddEventAdd.setOnClickListener {
                addEvent()
            }

        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.ADD_CALENDAR_EVENT_FRAGMENT)
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

    private fun addEvent() {

        val eventDetails = binding.editTextActivityAddEventDetails.text.toString()
        if (eventDetails.isBlank()) {
            sharedViewModel.showErrorDialog(getString(R.string.event_details_cannot_be_empty))
            return
        }

        DialogUtils.showConfirmAlertDialogInFragment(this@AddCalendarEventFragment,getString(R.string.addeventconfirmation), object :ConfirmDialogButtonClickListener{
            override fun onPositiveButtonClick() {
                val events = getEvents().apply {
                    add(CalendarEventObject(viewModel.selectedDate.value.toString(), eventDetails))
                }

                val json = Gson().toJson(events)

                savePreference(PreferenceKeys.EVENTS, json, object : SuccessListener {
                    override fun onSuccess() {
                        findNavController().popBackStack(R.id.cropCalendarFragment, false)
                    }
                })
            }

            override fun onNegativeButtonClick() {

            }
        })
    }
}