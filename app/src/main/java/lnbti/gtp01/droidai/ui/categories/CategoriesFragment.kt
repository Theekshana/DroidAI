package lnbti.gtp01.droidai.ui.categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.constants.StringConstants.HEADER_ICON
import lnbti.gtp01.droidai.constants.StringConstants.MENU_LIST
import lnbti.gtp01.droidai.constants.StringConstants.MENU_LIST_FRAGMENT
import lnbti.gtp01.droidai.constants.StringConstants.SUB_CATEGORY_LIST
import lnbti.gtp01.droidai.databinding.FragmentCategoriesBinding
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager
import lnbti.gtp01.droidai.utils.Utils

class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@CategoriesFragment
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CATEGORY_LIST_FRAGMENT)

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
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_categories)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =
            CategoryAdapter(Utils.getCategoryList(requireContext())) { homeItem ->
                adapterOnClick(
                    homeItem
                )
            }
    }

    private fun adapterOnClick(categoryItem: CategoryModel) {
        SharedPreferencesManager.savePreference(
            HEADER_ICON,
            categoryItem.image,
            object : SuccessListener {
                override fun onSuccess() {
                    if (categoryItem.type == MENU_LIST) {
                        sharedViewModel.setFragment(MENU_LIST_FRAGMENT)
                        findNavController().navigate(
                            CategoriesFragmentDirections.actionCategoriesFragmentToMenuFragment(
                                categoryItem
                            )
                        )

                    } else if (categoryItem.type == SUB_CATEGORY_LIST) {
                        sharedViewModel.setFragment(SUB_CATEGORY_LIST)
                        findNavController().navigate(
                            CategoriesFragmentDirections.actionCategoriesFragmentToSubCategoriesFragment(
                                categoryItem
                            )
                        )
                    }
                }
            })

    }
}