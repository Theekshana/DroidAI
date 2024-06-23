package lnbti.gtp01.droidai.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import lnbti.gtp01.droidai.R
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants.AGRI_INSPECTOR
import lnbti.gtp01.droidai.constants.StringConstants.LOGIN_FRAGMENT
import lnbti.gtp01.droidai.databinding.FragmentLoginBinding
import lnbti.gtp01.droidai.interfaces.SuccessListener
import lnbti.gtp01.droidai.ui.main.MainActivityViewModel
import lnbti.gtp01.droidai.utils.NetworkUtils
import lnbti.gtp01.droidai.utils.SharedPreferencesManager
import lnbti.gtp01.droidai.utils.SharedPreferencesManager.Companion.savePreference
import lnbti.gtp01.droidai.utils.Utils.Companion.getLoggedInUser

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    //Main Activity view model
    private lateinit var viewModel: LoginViewModel
    private lateinit var sharedViewModel: MainActivityViewModel
    private lateinit var binding: FragmentLoginBinding
    val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        FragmentLoginBinding.inflate(inflater, container, false).apply {
            binding = this
            lifecycleOwner = this@LoginFragment

            ViewModelProvider(requireActivity())[LoginViewModel::class.java].apply {
                //Data binding
                viewModel = this
                vm = this
                button.setOnClickListener {
                    if (NetworkUtils.isNetworkAvailable()) {
                        viewModel.loginDataChanged()
                    }else
                        sharedViewModel.showErrorDialog(getString(R.string.no_internet))
                }
            }

            textViewSignUp.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                )
            }

            facebookButton.setOnClickListener {
                sharedViewModel.showWarnDialog(getString(R.string.comingsoon))
            }

            googleButton.setOnClickListener {
                sharedViewModel.showWarnDialog(getString(R.string.comingsoon))
            }
        }

        //This Shared view model is using to update Main Activity layout changes from this fragment
        ViewModelProvider(requireActivity())[MainActivityViewModel::class.java].apply {
            sharedViewModel = this
            getLoggedInUser()?.let {
                setLoggedInUser(it)
                if (it.userRole == AGRI_INSPECTOR) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToInspectorHomeFragment())
                } else {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                }
            } ?: run {
                setFragment(LOGIN_FRAGMENT)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObservers()
    }

    private fun viewModelObservers() {
        viewModel.apply {
            loginResult.observe(viewLifecycleOwner) {
                it?.let {
                    sharedViewModel.setProgressDialogVisible(false)
                    if (it.success) {
                        it.data?.let { user ->
                            savePreference(
                                PreferenceKeys.LOGGED_IN_USER,
                                gson.toJson(user), object : SuccessListener {
                                    override fun onSuccess() {
                                        sharedViewModel.setLoggedInUser(user)
                                        if (user.userRole == AGRI_INSPECTOR) {
                                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToInspectorHomeFragment())
                                        } else {
                                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                                        }
                                    }
                                }
                            )
                        }
                    } else {
                        sharedViewModel.showErrorDialog(it.message)
                    }
                }
            }

            // Observer for login form state changes
            loginFormState.observe(requireActivity(), Observer {
                val loginState = it ?: return@Observer

                binding.apply {
                    if (loginState.userNameError != null) {
                        tilUsername.error = getString(loginState.userNameError)
                    }

                    if (loginState.passwordError != null) {
                        tilPassword.error = getString(loginState.passwordError)
                    }

                    if (loginState.isDataValid) {
                        if (NetworkUtils.isNetworkAvailable()) {
                            sharedViewModel.setProgressDialogVisible(true)
                            val firebaseToken =
                                SharedPreferencesManager.getPreference(PreferenceKeys.FIREBASE_TOKEN)
                            login(firebaseToken)
                        } else sharedViewModel.showErrorDialog(getString(R.string.no_internet))

                    }
                }

            })
        }

    }

    override fun onStop() {
        super.onStop()
        viewModel.loginResult.removeObservers(requireActivity())
        viewModel.loginFormState.removeObservers(requireActivity())
    }
}