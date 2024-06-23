package lnbti.gtp01.droidai.ui.inquiry

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.camerax.CamControl
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.constants.StringConstants.INQUIRY_FRAGMENT
import lnbti.gtp01.droidai.databinding.FragmentInquiriesBinding
import lnbti.gtp01.droidai.models.UserInquiry
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.UIUtils.Companion.changeUiSize

/**
 * Fragment for handling inquiries, capturing images, and submitting inquiries.
 *
 * This fragment allows users to input inquiry details, capture images, and submit inquiries.
 * It uses data binding with [FragmentInquiriesBinding] to interact with UI elements.
 * The user's input is validated, and inquiries are submitted through the [InquiryViewModel].
 *
 * @property TAG A tag for logging purposes.
 * @property binding Instance of [FragmentInquiriesBinding] for accessing UI elements.
 * @property viewModel Instance of [InquiryViewModel] for handling business logic.
 * @property userInquiry User instance to store user-related information.
 * @property imageUri The URI of the captured image.
 * @property activityResultLauncher Activity result launcher for capturing images.
 */
class InquiriesFragment : Fragment() {

    private lateinit var binding: FragmentInquiriesBinding
    private lateinit var viewModel: InquiryViewModel
    private var imageUri: Uri? = null
    private var userInquiry = UserInquiry()
    private lateinit var sharedViewModel: MainActivityViewModel

    /**
     * Activity result launcher for capturing images.
     * It handles the result of the activity launched for capturing an image
     * and calls the [setImage] function if the result is successful.
     */
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { setImage(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInquiriesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[InquiryViewModel::class.java]
        changeUiSize(requireContext(), binding.inquiryImage, 1, 1, 16)

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(INQUIRY_FRAGMENT)
        }

        //viewModelObservers()
        setupSpinner()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        binding.btnCamera.setOnClickListener {
            captureImage()
        }

        binding.btnSubmit.setOnClickListener {
            val status = "Pending"
            val problem = binding.etQuestion.text.toString()
            val district = binding.spinner.selectedItem.toString()
            val ageOfCrop = binding.etAgeOfCrop.text.toString()
            val imageUri = imageUri

            if (problem.isBlank() || ageOfCrop.isBlank()) {
                DialogUtils.showDialogWithoutActionInFragment(
                    this@InquiriesFragment,
                    DialogConstants.FAIL.value,
                    getString(R.string.fillAllFeilds)
                )

            }

            val inquiry = userInquiry.copy(
                createdUserNIC = sharedViewModel.loggedInUser.value?.nic,
                area = district,
                ageOfTheCrop = ageOfCrop,
                problem = problem,
                status = status
            )

            //Check Network connection
            if (NetworkUtils.isNetworkAvailable()) {
                imageUri?.let { viewModel.submitInquiry(inquiry, it) }
                sharedViewModel.setProgressDialogVisible(true)
                viewModel.submitInquiry.observe(viewLifecycleOwner) {
                    sharedViewModel.setProgressDialogVisible(false)
                    DialogUtils.showDialogWithoutActionInFragment(
                        this@InquiriesFragment,
                        DialogConstants.SUCCESS.value,
                        getString(R.string.successfullMessage)
                    )
                }

            } else {
                showErrorSnackbar()
            }

        }

    }

    private fun showErrorSnackbar() {
        Snackbar.make(binding.root, getString(R.string.no_internet), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.retry)) {
            }
            .show()
    }

    /**
     * Sets up the spinner in the user interface with district data from a resource array.
     * Configures the spinner adapter, dropdown layout, and item selection listener.
     */
    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.district,
            android.R.layout.simple_dropdown_item_1line
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    /**
     * Initiates the image capture process by launching the camera activity.
     * Upon capturing an image, the result is processed through the [activityResultLauncher].
     */
    private fun captureImage() {
        val cameraIntent = Intent(requireContext(), CamControl::class.java)
        activityResultLauncher.launch(cameraIntent)
    }

    /**
     * Sets the captured image to the [binding] ImageView using Glide.
     * Additionally, hides the capture button [binding] and stores the
     * selected image [imageUri] for later use.
     *
     * @param imageUri The URI of the captured image to be displayed.
     */
    private fun setImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.inquiryImage)
        // Save the selected image URI in a property for later use
        this.imageUri = imageUri
    }

    /**
     * Called when the fragment's view is destroyed.
     *
     * This is mainly used to reset toolbar behavior when the fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()

        // Reset toolbar behavior when the fragment is destroyed
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
        }
    }
}



