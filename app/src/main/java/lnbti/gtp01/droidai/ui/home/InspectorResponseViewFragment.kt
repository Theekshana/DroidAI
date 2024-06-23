package lnbti.gtp01.droidai.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentFarmerResponseBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel

/**
 *
 * This fragment displays the home screen with a list of inquiries.
 */
class InspectorResponseViewFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    lateinit var binding: FragmentFarmerResponseBinding
    val args: InspectorResponseViewFragmentArgs  by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        //val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentFarmerResponseBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
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

        val inquiry = args.details

        viewModel.viewInquiry(inquiry)

        viewModel.viewInquiryResult.observe(viewLifecycleOwner) { inquiry ->
            // Display the inquiry details
            binding.txtProblem.text = inquiry?.problem
            binding.txtAgeOfCrop.text = inquiry?.ageOfTheCrop
            binding.txtAnswer.text = inquiry?.response

            // Load and display the image associated with the inquiry
            Glide.with(requireContext())
                .load(inquiry?.imageUrl)
                .into(binding.inquiryImage)

        }
    }

}