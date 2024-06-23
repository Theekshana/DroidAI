package lnbti.gtp01.droidai.ui.airesponse

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.databinding.FragmentPendingBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel

/**
 *
 * This fragment displays the home screen with a list of inquiries.
 */
class PendingResponseFragment : Fragment() {

    private lateinit var viewModel: InspectorHomeViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var inquiryDataAdapter: InquiryResponseDataAdapter
    lateinit var binding: FragmentPendingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentPendingBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[InspectorHomeViewModel::class.java]

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
        }

        //return rootView
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe ViewModel for progress dialog
        viewModelObservers()

        // Set up RecyclerView
        setupRecyclerView()

        //clickListener
        inquiryDataAdapter.setOnItemClickListener {

            val bundle = Bundle().apply {
                putParcelable("inquiry", it)
            }

            findNavController().navigate(
                R.id.action_inspectorHomeFragment_to_inspectorResponseFragment, bundle
            )

        }

    }

    /**
     * Sets up the RecyclerView with the adapter and layout manager.
     */
    private fun setupRecyclerView() {
        inquiryDataAdapter = InquiryResponseDataAdapter()
        binding.recyclerViewInspectorHome.apply {
            adapter = inquiryDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    /**
     * Observe ViewModel for progress dialog.
     */
    private fun viewModelObservers() {

        // Observe LiveData for inquiry result data
        viewModel.inquiryResult.observe(viewLifecycleOwner) { inquiries ->
            inquiryDataAdapter.submitSortedList(inquiries)
            sharedViewModel.setProgressDialogVisible(false)

        }

    }

}