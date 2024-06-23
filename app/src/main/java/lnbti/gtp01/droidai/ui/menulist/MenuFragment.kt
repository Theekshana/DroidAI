package lnbti.gtp01.droidai.ui.menulist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import lnbti.gtp01.droidai.constants.PreferenceKeys.HEADER_ICON
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.constants.StringConstants.DetailsImage
import lnbti.gtp01.droidai.constants.StringConstants.DetailsPDF
import lnbti.gtp01.droidai.constants.StringConstants.FILTER
import lnbti.gtp01.droidai.databinding.FragmentMenuBinding
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * A fragment that displays crop calendar.
 *
 * This fragment includes custom toolbar behavior to control the appearance and behavior of the
 * toolbar in the context of notifications.
 */
class MenuFragment : Fragment() {
    private val args: MenuFragmentArgs by navArgs()
    lateinit var binding: FragmentMenuBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var menuAdapter: MenuAdapter
    var selectedItem: CategoryModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        selectedItem = args.item
        FragmentMenuBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@MenuFragment

            menuAdapter = MenuAdapter(selectedItem?.items,
                { itemModel -> adapterOnClick(itemModel) })
            recyclerView.adapter = menuAdapter
            val icon = SharedPreferencesManager.getPreference(HEADER_ICON)

            Glide.with(requireActivity()).load(icon)
                .into(headerIcon)

            title.text = selectedItem?.title
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.MENU_LIST_FRAGMENT)
        }

        return binding.root
    }

    private fun adapterOnClick(item: CategoryModel) {
        when (item.type) {
            StringConstants.MENU_LIST -> {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToMenuFragment(
                        item
                    )
                )
            }

            DetailsPDF, DetailsImage -> {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToContentDetailsFragment(
                        item
                    )
                )
            }

            FILTER -> {
                findNavController().navigate(
                    MenuFragmentDirections.actionMenuFragmentToContentDetailsWithFilterFragment(
                        item
                    )
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }
    }
}