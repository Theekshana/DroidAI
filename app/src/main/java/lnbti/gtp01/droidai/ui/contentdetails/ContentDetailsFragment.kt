package lnbti.gtp01.droidai.ui.contentdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.rajat.pdfviewer.PdfRendererView
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.PreferenceKeys.HEADER_ICON
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.constants.StringConstants.DetailsPDF
import lnbti.gtp01.droidai.databinding.FragmentContentDetailsBinding
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * A fragment that displays crop calendar.
 *
 * This fragment includes custom toolbar behavior to control the appearance and behavior of the
 * toolbar in the context of notifications.
 */
class ContentDetailsFragment : Fragment() {
    private val args: ContentDetailsFragmentArgs by navArgs()
    lateinit var binding: FragmentContentDetailsBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    var selectedItem: CategoryModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        selectedItem = args.item
        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CONTENT_DETAILS_FRAGMENT)
        }

        FragmentContentDetailsBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@ContentDetailsFragment
            val icon = SharedPreferencesManager.getPreference(HEADER_ICON)
            Glide.with(requireActivity()).load(icon)
                .into(headerIcon)
            title.text = selectedItem?.title
            if (selectedItem?.type == DetailsPDF) {
                detailsView.isVisible = false
                pdfViewActivityPdf.isVisible = true
                sharedViewModel.setProgressDialogVisible(true)
                sharedViewModel.setProgressAmount(0)
                loadPDF(selectedItem?.link!!)
            } else {
                detailsView.isVisible = true
                pdfViewActivityPdf.isVisible = false
                Glide.with(this@ContentDetailsFragment).load(selectedItem?.link)
                    .into(binding.detailsView)
            }
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

    private fun loadPDF(pdfUrl: String) {
        val pdfView = binding.pdfViewActivityPdf
        pdfView.statusListener = object : PdfRendererView.StatusCallBack {
            override fun onPdfLoadSuccess(absolutePath: String) {
                super.onPdfLoadSuccess(absolutePath)
                sharedViewModel.setProgressDialogVisible(false)
            }

            override fun onError(error: Throwable) {
                super.onError(error)
                sharedViewModel.setProgressDialogVisible(false)
                sharedViewModel.showErrorDialog(getString(R.string.failed_to_load_details))
            }

            override fun onPdfLoadProgress(
                progress: Int,
                downloadedBytes: Long,
                totalBytes: Long?,
            ) {
                sharedViewModel.setProgressAmount(progress)
            }
        }
        pdfView.initWithUrl(
            url = pdfUrl,
            lifecycleCoroutineScope = lifecycleScope,
            lifecycle = lifecycle
        )
    }

}