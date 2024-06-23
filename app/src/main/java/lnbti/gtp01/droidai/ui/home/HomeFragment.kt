package lnbti.gtp01.droidai.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentHomeBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showConfirmAlertDialogInFragment
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.Utils.Companion.getLoggedInUser

/**
 *
 * This fragment displays the home screen with a list of inquiries.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var inquiryDataAdapter: InquiryDataAdapter
    lateinit var binding: FragmentHomeBinding
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        FragmentHomeBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@HomeFragment
            getLoggedInUser()?.let {
                textViewFragmentHomeName.text = it.getName()
            }

        }  //return rootView

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

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
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null)
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.HOME_FRAGMENT)
        }


        ViewModelProvider(requireActivity())[HomeViewModel::class.java].apply {
            viewModel = this

            // Observe ViewModel for progress dialog
            viewModelObservers()

            // Set up RecyclerView
            setupRecyclerView()

            //val user = "911111111v"
            val user = sharedViewModel.loggedInUser.value?.nic
            // Set phone number to dial
            val phoneNumber = "0112111222"

            // Fetch data if network is available
            if (NetworkUtils.isNetworkAvailable()) {
                if (user != null) {
                    viewModel.fetchData(user)
                }
                sharedViewModel.setProgressDialogVisible(true)
            } else {
                // If no network, show error message
                showErrorSnackbar(view)
            }

            binding.callLayout.setOnClickListener {
                showConfirmAlertDialogInFragment(
                    this@HomeFragment,
                    getString(R.string.contactconfirmation),
                    object : ConfirmDialogButtonClickListener {
                        override fun onPositiveButtonClick() {
                            viewModel.setPhoneNumberToDial(phoneNumber)
                        }

                        override fun onNegativeButtonClick() {

                        }
                    })
            }

            inquiryDataAdapter.setOnItemClickListener {

                val bundle = Bundle().apply {
                    putParcelable("details", it)
                }

                findNavController().navigate(
                    R.id.action_homeFragment_to_inspectorResponseViewFragment,
                    bundle
                )

            }

        }
    }

    /**
     * Initiates a phone call using the provided phone number.
     */
    private fun initiatePhoneCall(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }

    /**
     * Sets up the RecyclerView with the adapter and layout manager.
     */
    private fun setupRecyclerView() {
        inquiryDataAdapter = InquiryDataAdapter()

        binding.recyclerViewHome.apply {
            adapter = inquiryDataAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /**
     * Shows a Snackbar with a message to indicate no internet connection.
     */
    private fun showErrorSnackbar(view: View) {
        val snackbar = Snackbar.make(
            view.rootView,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.retry)) {
            // Handle retry action here
        }

        val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
        // Adjust bottom margin to accommodate the bottom navigation bar
        params.setMargins(0, 0, 0, 100)
        snackbar.view.layoutParams = params

        snackbar.show()
    }

    /**
     * Observe ViewModel for progress dialog.
     */
    private fun viewModelObservers() {

        // Observe LiveData for phone number to dial
        viewModel.phoneNumberToDial.observe(viewLifecycleOwner) { phoneNumber ->
            phoneNumber?.let {
                initiatePhoneCall(phoneNumber)
            }

        }

        // Observe LiveData for inquiry result data
        viewModel.inquiryResult.observe(viewLifecycleOwner) { inquiries ->
            inquiryDataAdapter.submitSortedList(inquiries)
            sharedViewModel.setProgressDialogVisible(false)
        }
    }

}

