package lnbti.gtp01.droidai.ui.airesponse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import lnbti.gtp01.droidai.databinding.FragmentRespondedBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel

/**
 * Fragment responsible for displaying responded inquiries.
 */
class FragmentResponded : Fragment() {

    // View binding for the fragment layout
    lateinit var binding: FragmentRespondedBinding

    // ViewModel instance
    private lateinit var viewModel: InspectorHomeViewModel

    // Adapter for the RecyclerView
    private lateinit var inquiryDataAdapter: InquiryResponseDataAdapter
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRespondedBinding.inflate(inflater, container, false)
        // Initialize the ViewModel
        viewModel = ViewModelProvider(requireActivity())[InspectorHomeViewModel::class.java]


        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
        }
        //return rootView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up observers for ViewModel LiveData
        viewModelObservers()

        // Set up RecyclerView
        setupRecyclerView()
    }

    /**
     * Set up observers for ViewModel LiveData.
     * Observes LiveData for progress dialog visibility and inquiry result data.
     */
    private fun viewModelObservers() {

        // Observe LiveData for inquiry result data
        viewModel.inquiryResult.observe(viewLifecycleOwner) { inquiries ->
            inquiries?.let {
                // Pass the list of inquiries to the adapter for displaying responded inquiries
                inquiryDataAdapter.filterResponseCompletedList(inquiries)
                sharedViewModel.setProgressDialogVisible(false)

            }
        }

    }

    /**
     * Set up the RecyclerView.
     * Initialize the adapter and attach it to the RecyclerView.
     */
    private fun setupRecyclerView() {
        inquiryDataAdapter = InquiryResponseDataAdapter()

        binding.recyclerViewInspectorHomeResponded.apply {
            adapter = inquiryDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}