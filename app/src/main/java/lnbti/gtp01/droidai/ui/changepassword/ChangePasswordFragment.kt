package lnbti.gtp01.droidai.ui.changepassword

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.databinding.FragmentChangePasswordBinding
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.LanguageManager
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.UIUtils

class ChangePasswordFragment : Fragment() {
    lateinit var binding: FragmentChangePasswordBinding
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageManager(requireContext()).loadLanguage()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ChangePasswordFragment
            ViewModelProvider(requireActivity())[ChangePasswordViewModel::class.java].apply {
                //Data binding
                viewModel = this
                vm = this
                button.setOnClickListener {
                    viewModel.changePasswordDataChanged()
                }
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            setFragment(StringConstants.CHANGE_PASSWORD_FRAGMENT)

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

        // Observer for login form state changes
        viewModel.changePasswordForm.observe(requireActivity(), Observer {
            val state = it ?: return@Observer

            binding.apply {
                if (state.oldPasswordError != null) {
                    tilOldPassword.error = getString(state.oldPasswordError)
                } else
                    UIUtils.validState(requireContext(), tilOldPassword, R.drawable.ic_check)

                if (state.newPasswordError != null) {
                    tilNewPassword.error = getString(state.newPasswordError)
                } else
                    UIUtils.validState(requireContext(), tilNewPassword, R.drawable.ic_check)

                if (state.confirmPasswordError != null) {
                    tilConfirmPassword.error = getString(state.confirmPasswordError)
                } else
                    UIUtils.validState(requireContext(), tilConfirmPassword, R.drawable.ic_check)

                if (state.isDataValid) {
                    if (NetworkUtils.isNetworkAvailable()) {
                        sharedViewModel.showSuccessDialog(getString(R.string.passwordchangesuccess))
                        findNavController().popBackStack()
                    } else sharedViewModel.showErrorDialog(getString(R.string.no_internet))
                }
            }
        })
    }
}