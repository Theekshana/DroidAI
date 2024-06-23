package lnbti.gtp01.droidai.ui.dealerlocations

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.delay
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.DialogDealerLocationViewBinding
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager

/**
 * Dealer Location View Dialog with Google Map Fragment
 */
class LocationDialogFragment : DialogFragment() {

    private lateinit var binding: DialogDealerLocationViewBinding
    lateinit var context: Activity
    lateinit var locationUrl: String
    private lateinit var sharedViewModel: MainActivityViewModel

    companion object {
        private const val LOCATION_URL = "url"
        private lateinit var successListener: SuccessListener
        fun newInstance(
            context: Activity,
            locationUrl: String,
            listener: SuccessListener
        ): LocationDialogFragment {
            return LocationDialogFragment().apply {
                this.context = context
                this.locationUrl = locationUrl
                successListener = listener
                arguments = Bundle().apply {
                    putString(LOCATION_URL, locationUrl)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CATEGORY_LIST_FRAGMENT)
            setProgressDialogVisible(true)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(
            requireContext(),
            android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen
        ).apply {
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Disable back button pressed dialog dismiss event
        isCancelable = false
        binding = DialogDealerLocationViewBinding.inflate(inflater, container, false).apply {
            // vm = viewModel
            lifecycleOwner = this@LocationDialogFragment
            webView.settings.javaScriptEnabled = true
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            // Initialize the WebViewClient
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    // Call setProgressDialogVisible(false) after web view finished loading
                    sharedViewModel.setProgressDialogVisible(false)
                }
            }
            webView.loadUrl(locationUrl)
            leftButton.setOnClickListener {
                dismiss()
            }
        }
    }
}