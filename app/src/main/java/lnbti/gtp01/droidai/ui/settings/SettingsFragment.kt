package lnbti.gtp01.droidai.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants.SETTINGS_FRAGMENT
import lnbti.gtp01.droidai.databinding.FragmentSettingsBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.updateSelectedLanguage

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    //Main Activity view model
    private lateinit var sharedViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@SettingsFragment
            ViewModelProvider(requireActivity())[SettingsViewModel::class.java].apply {
                viewModel = this
                vm = this
            }

            changePasswordSection.setOnClickListener {
                findNavController().navigate(
                    SettingsFragmentDirections.actionSettingsFragmentToChangePasswordFragment()
                )
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(SETTINGS_FRAGMENT)
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
//Get selected language form preference and set to live data
        SharedPreferencesManager.getSelectedLanguage().apply {
            viewModel.setSelectedLanguage(
                this
            )
        }

        // When clicking on the languages layout, change the live selected language live data value
        // The updated value should be saved in the preference
        viewModel.apply {
            selectedLanguage.observe(viewLifecycleOwner) {
                // Update the selected value in the preference
                updateSelectedLanguage(it)
                // Set language preference
                val languageManager = LanguageManager(requireContext())
                languageManager.loadLanguage()
                setSelectedLanguageLabels(
                    getString(
                        R.string.settings
                    ), getString(
                        R.string.change_language
                    ), getString(
                        R.string.change_password
                    )
                )

                sharedViewModel.setUpdateLabels(true)
            }
        }
    }

}