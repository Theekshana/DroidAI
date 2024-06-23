package lnbti.gtp01.droidai.ui.subcategory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentSubCategoriesBinding
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.ui.categories.CategoryAdapter
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * A fragment that displays crop calendar.
 *
 * This fragment includes custom toolbar behavior to control the appearance and behavior of the
 * toolbar in the context of notifications.
 */
class SubCategoryFragment : Fragment() {
    private val args: SubCategoryFragmentArgs by navArgs()
    lateinit var binding: FragmentSubCategoriesBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    var selectedItem: CategoryModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        selectedItem = args.item
        binding = FragmentSubCategoriesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@SubCategoryFragment
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.SUB_CATEGORY_LIST_FRAGMENT)
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Custom toolbar behavior for this fragment
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setHomeAsUpIndicator(null) // Hide back button indication
        }

        selectedItem?.items?.let {
            binding.recyclerViewSubCategories.adapter =
                CategoryAdapter(it) { homeItem ->
                    adapterOnClick(
                        homeItem
                    )
                }
        }
    }

    private fun adapterOnClick(categoryItem: CategoryModel) {
        SharedPreferencesManager.savePreference(
            StringConstants.HEADER_ICON,
            categoryItem.image,
            object : SuccessListener {
                override fun onSuccess() {
                    if (categoryItem.type == StringConstants.MENU_LIST) {
                        findNavController().navigate(
                            SubCategoryFragmentDirections.actionSubCategoriesFragmentToMenuFragment(
                                categoryItem
                            )
                        )

                    }
                }
            })

    }
}