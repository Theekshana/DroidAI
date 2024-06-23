package lnbti.gtp01.droidai.ui.agricinspector

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentAgricInspectorBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel

/**
 * Fragment for displaying a list of agricultural inspectors.
 *
 * This fragment is responsible for displaying a list of agricultural inspectors retrieved
 * from the ViewModel. It uses a RecyclerView and an adapter to efficiently display the data.
 */
@AndroidEntryPoint
class AgricInspectorsFragment : Fragment() {

    private lateinit var binding: FragmentAgricInspectorBinding
    private val viewModel: AgricInspectorsViewModel by viewModels()
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAgricInspectorBinding.inflate(inflater, container, false)
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.EXPERTS_FRAGMENT)
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

        val adapter = AgricInspectorListAdapter { inspector ->
            // Handle item click event here
            initiatePhoneCall(inspector.contactNo)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.inspectorsLiveData.observe(viewLifecycleOwner) { inspectors ->
            inspectors?.let {
                adapter.submitList(it)
            }
        }
    }

    private fun initiatePhoneCall(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(dialIntent)
    }
}
