package lnbti.gtp01.droidai.ui.dealerlocations

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentDealerLocationsBinding
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.models.Dealer
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.Utils.Companion.getDealerList

class DealerLocationsFragment : Fragment() {
    private var initialSelection = true
    private lateinit var binding: FragmentDealerLocationsBinding
    private lateinit var viewModel: DealerLocationsViewModel
    private lateinit var dealerLocationsListAdapter: DealerLocationsListAdapter
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentDealerLocationsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@DealerLocationsFragment
            ViewModelProvider(requireActivity())[DealerLocationsViewModel::class.java].apply {
                viewModel = this
                vm = this
                setAllDealers(getDealerList(requireContext()))
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CATEGORY_LIST_FRAGMENT)

        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }
        initiateAdapter()

        // Observe LiveData for inquiry result data
        viewModel.dealerList.observe(viewLifecycleOwner) { inquiries ->
            inquiries?.let {
                dealerLocationsListAdapter.setItems(it)
            }
        }
    }

    private fun initiateAdapter() {
        // Initiate the RecyclerView Adapter

        DealerLocationsListAdapter(itemClickListener = object :
            DealerLocationsListAdapter.OnItemClickListener {
            override fun onItemClick(item: Dealer) {
                parentFragmentManager.let { fragmentManager ->
                    LocationDialogFragment.newInstance(
                        requireActivity(),
                        item.locationUrl,
                        object : SuccessListener {
                            override fun onSuccess() {
                            }

                        }
                    )
                        .show(
                            fragmentManager,
                            DialogConstants.LOCATION_VIEW_DIALOG_FRAGMENT_TAG.value
                        )
                }
            }

        }).apply {
            dealerLocationsListAdapter = this
            /* Set Adapter to Recycle View */
            binding.recyclerView.also { it2 ->
                it2.adapter = this
            }
        }

        binding.apply {
            ArrayAdapter.createFromResource(
                requireActivity(),
                R.array.district_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                spinner.onItemSelectedListener
            }
            // Handle spinner item selection
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    if (!initialSelection) {
                        val selectedItem = parent?.getItemAtPosition(position).toString()
                        viewModel.filterUsersByDistrict(getString(R.string.all),selectedItem)
                    }
                    initialSelection = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected
                }
            }
        }
    }
}