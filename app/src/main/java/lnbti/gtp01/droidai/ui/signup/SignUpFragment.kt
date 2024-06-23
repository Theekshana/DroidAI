package lnbti.gtp01.droidai.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentSignupBinding
import lnbti.gtp01.droidai.interfaces.ConfirmDialogButtonClickListener
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.DialogUtils.Companion.showConfirmAlertDialog
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.UIUtils

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {
    //Main Activity view model
    private lateinit var viewModel: SignUpViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: FragmentSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        FragmentSignupBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@SignUpFragment

            ViewModelProvider(requireActivity())[SignUpViewModel::class.java].apply {
                //Data binding
                viewModel = this
                vm = this
                button.setOnClickListener {
                    if (NetworkUtils.isNetworkAvailable())
                        viewModel.registerDataChanged()
                    else
                        sharedViewModel.showErrorDialog(getString(R.string.no_internet))
                }
            }

            leftButton.setOnClickListener {
                navigateToLoginFragment()
            }

        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.SIGN_UP_FRAGMENT)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObservers()
    }

    private fun viewModelObservers() {
        viewModel.apply {
            registerResult.observe(requireActivity()) {
                it?.let {
                    sharedViewModel.setProgressDialogVisible(false)
                    if (it.success) {
                        navigateToLoginFragment()
                    } else {
                        sharedViewModel.showErrorDialog(it.message)
                    }

                }
            }

            // Observer for login form state changes
            registerFormState.observe(requireActivity(), Observer {
                val loginState = it ?: return@Observer

                binding.apply {
                    if (loginState.nicError != null) {
                        tilNICName.error = getString(loginState.nicError)
                    } else
                        UIUtils.validState(requireContext(), tilNICName, R.drawable.ic_check)


                    if (loginState.firstNameError != null) {
                        tilFirstName.error = getString(loginState.firstNameError)
                    } else
                        UIUtils.validState(requireContext(), tilFirstName, R.drawable.ic_check)


                    if (loginState.lastNameError != null) {
                        tilLastName.error = getString(loginState.lastNameError)
                    } else
                        UIUtils.validState(requireContext(), tilLastName, R.drawable.ic_check)

                    if (loginState.contactNoError != null) {
                        tilContactNo.error = getString(loginState.contactNoError)
                    } else
                        UIUtils.validState(requireContext(), tilContactNo, R.drawable.ic_check)

                    if (loginState.userNameError != null) {
                        tilUsername.error = getString(loginState.userNameError)
                    } else
                        UIUtils.validState(requireContext(), tilUsername, R.drawable.ic_check)


                    if (loginState.passwordError != null) {
                        tilPassword.error = getString(loginState.passwordError)
                    }

                    if (loginState.isDataValid) {

                        showConfirmAlertDialog(requireContext(),
                            getString(R.string.confirm_register), object :
                                ConfirmDialogButtonClickListener {
                                override fun onPositiveButtonClick() {
                                    if (NetworkUtils.isNetworkAvailable()) {
                                        sharedViewModel.setProgressDialogVisible(true)
                                        register()
                                    } else sharedViewModel.showErrorDialog(getString(R.string.no_internet))
                                }

                                override fun onNegativeButtonClick() {

                                }
                            })
                    }
                }

            })
        }

    }

    private fun navigateToLoginFragment() {
        findNavController().popBackStack(R.id.loginFragment, false)
    }
}