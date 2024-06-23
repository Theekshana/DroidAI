package lnbti.gtp01.droidai.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentWelcomeBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    //Main Activity view model
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: FragmentWelcomeBinding
    lateinit var viewModel: WelcomeFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentWelcomeBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@WelcomeFragment
            ViewModelProvider(this@WelcomeFragment)[WelcomeFragmentViewModel::class.java].apply {
                viewModel = this
                vm = this

                SharedPreferencesManager.getSelectedLanguage().apply {
                    setSelectedLanguage(
                        this
                    )
                }

                selectedLanguage.observe(requireActivity()) {
                    // Update the selected value in the preference
                    SharedPreferencesManager.updateSelectedLanguage(it)
                    // Set language preference
                    val languageManager = LanguageManager(requireContext())
                    languageManager.loadLanguage()
                    setSelectedLanguageLabels(
                        getString(R.string.landing_page_content),
                        getString(R.string.next)
                    )
                }
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
                SharedPreferencesManager.getPreferenceBool(PreferenceKeys.APP_LAUNCHED_STATUS)
                    ?.let {
                        if(it) {
                            //Already launched the app
                            navigateToLoginFragment()
                        }else{
                            sharedViewModel = this
                            setFragment(StringConstants.WELCOME_FRAGMENT)
                        }

                    } ?: run {
                    //If app is launched for the first time
                    sharedViewModel = this
                    setFragment(StringConstants.WELCOME_FRAGMENT)
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            SharedPreferencesManager.savePreferenceBool(
                PreferenceKeys.APP_LAUNCHED_STATUS,
                true
            )
            navigateToLoginFragment()
        }
    }

    private fun navigateToLoginFragment() {
        findNavController().navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment()
        )
    }
}