package lnbti.gtp01.droidai.ui.airesponse

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentInspectorHomeBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.Utils.Companion.getLoggedInUser

/**
 *
 * This fragment displays the home screen with a list of inquiries.
 * Provides navigation tabs for pending and completed inquiries.
 */
class InspectorHomeFragment : Fragment() {

    // ViewModel for the fragment
    private lateinit var viewModel: InspectorHomeViewModel
    private lateinit var sharedViewModel: MainActivityViewModel

    // View binding for the fragment layout
    lateinit var binding: FragmentInspectorHomeBinding

    // Adapter for the ViewPager2
    private lateinit var adapter: FragmentPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInspectorHomeBinding.inflate(inflater, container, false)

        // Initialize ViewModel
        viewModel = ViewModelProvider(requireActivity())[InspectorHomeViewModel::class.java]

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.AI_HOME_FRAGMENT)
        }

        //setup tab layout
        setupTabLayout()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        getLoggedInUser()?.let {
            binding.textViewFragmentHomeName.text = it.getName()
        }

        // Fetch data
        fetchInquiryData()

    }

    /**
     * Fetches data from the ViewModel if network is available; otherwise, shows error Snackbar.
     */
    private fun fetchInquiryData() {
        if (NetworkUtils.isNetworkAvailable()) {
            viewModel.fetchDataForInspectors()
            sharedViewModel.setProgressDialogVisible(true)
        } else {
            view?.let { showErrorSnackbar(it) }
        }
    }

    /**
     * Sets up the tab layout and ViewPager2.
     */
    private fun setupTabLayout() {
        adapter = FragmentPageAdapter(childFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter

        // Custom tab views for pending and completed tabs
        binding.tabLayout.apply {
            val pendingTab = newTab().apply {
                setCustomView(R.layout.pending_custom_tab)
                addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        setPendingTabTextSize(tab, 24f)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {
                        setPendingTabTextSize(tab, 15f)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })
            }

            val completedTab = newTab().apply {
                setCustomView(R.layout.completed_custom_tab)
                addOnTabSelectedListener(object : OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab) {
                        setCompleteTabTextSize(tab, 24f)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab) {
                        setCompleteTabTextSize(tab, 15f)
                    }

                    override fun onTabReselected(tab: TabLayout.Tab) {}
                })
            }

            addTab(pendingTab)
            addTab(completedTab)
        }

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    binding.viewPager2.currentItem = it.position
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        binding.viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.getTabAt(position)?.select()

            }
        })
    }

    /**
     * Sets the text size for a tab's custom view.
     */
    private fun setPendingTabTextSize(tab: TabLayout.Tab?, textSize: Float) {
        tab?.let {
            val customView = it.customView
            val textView = customView?.findViewById<TextView>(R.id.txtPending)
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        }
    }

    /**
     * Sets the text size for a tab's custom view.
     */
    private fun setCompleteTabTextSize(tab: TabLayout.Tab?, textSize: Float) {
        tab?.let {
            val customView = it.customView
            val textView = customView?.findViewById<TextView>(R.id.txtComplete)
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)

        }
    }

    /**
     * Shows a Snackbar with a message to indicate no internet connection.
     */
    private fun showErrorSnackbar(view: View) {
        Snackbar.make(
            view,
            getString(R.string.no_internet),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.retry)) {
            onViewCreated(view, null)
        }.show()
    }

}