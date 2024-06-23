package lnbti.gtp01.droidai.ui.airesponse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.DialogConstants
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentResponseBinding
import lnbti.gtp01.droidai.models.SubmitResponseRequest
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils

/**
 *
 * This fragment displays the home screen with a list of inquiries.
 */
class InspectorResponseFragment : Fragment() {

    private lateinit var viewModel: InspectorHomeViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var inquiryDataAdapter: InquiryResponseDataAdapter
    lateinit var binding: FragmentResponseBinding
    val args: InspectorResponseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentResponseBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[InspectorHomeViewModel::class.java]

        inquiryDataAdapter = InquiryResponseDataAdapter()

        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.RESPONSE_VIEW_FRAGMENT)
        }

        //return rootView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        val inquiry = args.inquiry

        viewModel.viewInquiry(inquiry)

        viewModel.viewInquiryResult.observe(viewLifecycleOwner) { inquiry ->

            binding.apply {
                txtProblem.text = inquiry.problem
                txtAgeOfCrop.text = inquiry.ageOfTheCrop
                Glide.with(requireContext())
                    .load(inquiry?.imageUrl)
                    .into(inquiryImage)

                Log.d("Args", "Data: $inquiry")
            }

        }

        binding.btnSubmit.setOnClickListener {

            val aiResponse = binding.etAnswer.text.toString()
            //val inquiry = args.inquiry
            val id = inquiry.id
            //val id = "911111111v"
            val repspondedUserNIC = "911240807v"
            val status = "Completed"

            if (aiResponse.isBlank()) {
                DialogUtils.showDialogWithoutActionInFragment(
                    this@InspectorResponseFragment,
                    DialogConstants.FAIL.value,
                    getString(R.string.fillTheAnswerFeild)
                )
                return@setOnClickListener

            }

            val aiRes = SubmitResponseRequest(
                id = id,
                respondedUserNIC = repspondedUserNIC,
                response = aiResponse,
                status = status
            )
            //viewModel.updateWithAiResponse(aiRes)
            Log.d("Args", "Data: $aiRes")

            if (aiResponse.isNotEmpty()) {
                inquiry.response = aiResponse
                viewModel.updateWithAiResponse(aiRes)
                DialogUtils.showDialogWithoutActionInFragment(
                    this@InspectorResponseFragment,
                    DialogConstants.SUCCESS.value,
                    getString(R.string.successfullMessage)
                )
                navigateToAiHomePage()
                Log.d("Args", "New Data: $inquiry")
            }

        }

    }

    private fun navigateToAiHomePage() {
        findNavController().navigate(R.id.inspectorHomeFragment)
    }


}